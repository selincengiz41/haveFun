package com.selincengiz.havefun.ui.user.home

import android.annotation.SuppressLint
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mindinventory.midrawer.MIDrawerView
import com.selincengiz.havefun.R
import com.selincengiz.havefun.common.Extensions.loadUrl
import com.selincengiz.havefun.common.HomeState
import com.selincengiz.havefun.common.PermissionUtils
import com.selincengiz.havefun.common.PermissionUtils.checkPermission
import com.selincengiz.havefun.common.PermissionUtils.shouldShowRationale
import com.selincengiz.havefun.data.model.GetEventsByCategoriesRequest
import com.selincengiz.havefun.data.model.GetEventsRequest
import com.selincengiz.havefun.data.model.SearchRequest
import com.selincengiz.havefun.databinding.FragmentHomeBinding
import com.selincengiz.havefun.ui.adapter.CategoryAdapter
import com.selincengiz.havefun.ui.adapter.EventAdapter
import com.selincengiz.havefun.ui.adapter.ItemCategoryListener
import com.selincengiz.havefun.ui.adapter.ItemEventListener
import com.selincengiz.havefun.ui.user.map.MapFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(), ItemCategoryListener, ItemEventListener,
    android.widget.SearchView.OnQueryTextListener, View.OnClickListener,
    View.OnFocusChangeListener {
    @Inject
    lateinit var auth: FirebaseAuth

    @Inject
    lateinit var db: FirebaseFirestore
    private lateinit var binding: FragmentHomeBinding
    private val viewModel by viewModels<HomeViewModel>()
    private val categoryAdapter by lazy { CategoryAdapter(this, "Home") }
    private val eventAdapter by lazy { EventAdapter(this) }
    private lateinit var flpc: FusedLocationProviderClient
    private lateinit var locationTask: Task<Location>
    private lateinit var location: com.google.android.gms.maps.model.LatLng

    @SuppressLint("MissingPermission")
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->

            if (isGranted) {

                locationTask = flpc.lastLocation
                getLocation()

            } else {
                findNavController().navigateUp()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        flpc = LocationServices.getFusedLocationProviderClient(requireActivity())
        binding.homeFunctions = this

        binding.categoriesRecycler.adapter = categoryAdapter
        binding.popularRecycler.adapter = eventAdapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestPermission()
        binding.searchView.setOnQueryTextListener(this)
        binding.searchView.setOnQueryTextFocusChangeListener(this)
        with(binding) {

            profile2.loadUrl(auth.currentUser?.photoUrl)
            name = auth.currentUser?.displayName

        }


        binding.layout.setOnClickListener {
            binding.searchView.clearFocus()
        }


        viewModel.getCategories()

        observe()

        // Set color for the container's content as transparent
        binding.drawerLayout.setScrimColor(Color.TRANSPARENT)

        binding.logout2.setOnClickListener(this)
        binding.myProfile.setOnClickListener(this)
        binding.attendedEvents.setOnClickListener(this)



        // Implement the drawer listener
        binding.drawerLayout.setMIDrawerListener(object : MIDrawerView.MIDrawerEvents {
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)

            }

            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(drawerView)

            }
        })

    }


    fun getLocation() {
        locationTask.addOnSuccessListener {
            location = LatLng(it.latitude, it.longitude)
            var getEventsRequest = GetEventsRequest(location.latitude, location.longitude)
            viewModel.getEvents(getEventsRequest)
        }
    }

    fun requestPermission() {
        requireContext().checkPermission(
            PermissionUtils.locationFinePermission,
            onGranted = {
                locationTask = flpc.lastLocation
                getLocation()
            },
            onDenied = {
                requestPermissionLauncher.launch(PermissionUtils.locationFinePermission)
            }
        )

        requireActivity().shouldShowRationale(
            PermissionUtils.locationFinePermission,
            onGranted = {
                Toast.makeText(
                    requireContext(),
                    R.string.permission_required_location,
                    Toast.LENGTH_SHORT
                ).show()

                requestPermissionLauncher.launch(
                    PermissionUtils.locationFinePermission
                )
            },
        )

    }

    fun observe() {
        viewModel.homeState.observe(viewLifecycleOwner) { state ->

            when (state) {

                is HomeState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.mainLayout.visibility = View.GONE
                    binding.categoriesRecycler.visibility= View.GONE
                }

                is HomeState.ApiCategory -> {

                    binding.progressBar.visibility = View.GONE
                    binding.mainLayout.visibility = View.VISIBLE
                    binding.categoriesRecycler.visibility= View.VISIBLE
                    categoryAdapter.submitList(state.categories)
                }

                is HomeState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.mainLayout.visibility = View.GONE
                    binding.categoriesRecycler.visibility= View.GONE
                    Toast.makeText(requireContext(), state.throwable.message, Toast.LENGTH_SHORT)
                        .show()
                }

                is HomeState.ApiEvents -> {
                    binding.progressBar.visibility = View.GONE
                    binding.mainLayout.visibility = View.VISIBLE
                    binding.categoriesRecycler.visibility= View.VISIBLE
                    eventAdapter.submitList(state.events)
                }


                else -> {

                }
            }

        }
    }

    fun backClicked() {

    }

    fun logOut() {
        auth.signOut()
        findNavController().navigate(HomeFragmentDirections.actionGlobalSplashFragment())
    }

    override fun onClicked(category: Int) {
        if (category == 7) {
            var getEventsRequest = GetEventsRequest(location.latitude, location.longitude)
            viewModel.getEvents(getEventsRequest)
        } else {
            var getEventsByCategoriesRequest =
                GetEventsByCategoriesRequest(
                    category,
                    location.latitude,
                    location.longitude
                )
            viewModel.getEventsByCategories(getEventsByCategoriesRequest)
        }
    }


    fun avoidDoubleClicks(view: View) {
        val DELAY_IN_MS: Long = 900
        if (!view.isClickable) {
            return
        }
        view.isClickable = false
        view.postDelayed({ view.isClickable = true }, DELAY_IN_MS)
    }

    override fun onClickedEvent(event: Int) {
        findNavController().navigate(HomeFragmentDirections.homeToDetail(event))
    }

    override fun onQueryTextSubmit(text: String?): Boolean {
        text?.let {
            if (it.length > 3) {
                var searchRequest = SearchRequest(it)
                viewModel.search(searchRequest)
            }
        }
        return true
    }

    override fun onQueryTextChange(text: String?): Boolean {
        text?.let {
            if (it.length > 3) {
                var searchRequest = SearchRequest(it)
                viewModel.search(searchRequest)
            }
        }

        return true
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.my_profile -> {
                avoidDoubleClicks(binding.myProfile)
findNavController().navigate(HomeFragmentDirections.actionGlobalProfileFragment())
            }

            R.id.attended_events -> {
                avoidDoubleClicks(binding.attendedEvents)
                findNavController().navigate(HomeFragmentDirections.actionGlobalAttendedFragment())
            }

            R.id.logout2 -> {
                avoidDoubleClicks(binding.logout2)

            }

        }
    }

    override fun onFocusChange(p0: View?, p1: Boolean) {
        val listener: MapFragment.BottomNavListener = activity as MapFragment.BottomNavListener
        listener.bottomNavListenerEvent(p1)
    }


}