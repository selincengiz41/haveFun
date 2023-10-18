package com.selincengiz.havefun.ui.user.home

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.selincengiz.havefun.R
import com.selincengiz.havefun.common.Extensions.loadUrl
import com.selincengiz.havefun.common.HomeState
import com.selincengiz.havefun.common.PermissionUtils
import com.selincengiz.havefun.common.PermissionUtils.checkPermission
import com.selincengiz.havefun.common.PermissionUtils.shouldShowRationale
import com.selincengiz.havefun.databinding.FragmentHomeBinding
import com.selincengiz.havefun.ui.adapter.CategoryAdapter
import com.selincengiz.havefun.ui.adapter.EventAdapter
import com.selincengiz.havefun.ui.adapter.ItemCategoryListener
import com.selincengiz.havefun.ui.adapter.ItemEventListener
import com.selincengiz.havefun.ui.user.login.LoginViewModel
import com.selincengiz.havefun.ui.user.map.MapFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(), ItemCategoryListener, ItemEventListener,
    android.widget.SearchView.OnQueryTextListener {
    @Inject
    lateinit var auth: FirebaseAuth

    @Inject
    lateinit var db: FirebaseFirestore
    private lateinit var binding: FragmentHomeBinding
    private val viewModel by viewModels<HomeViewModel>()
    private val categoryAdapter by lazy { CategoryAdapter(this) }
    private val eventAdapter by lazy { EventAdapter(this, db) }
    private val eventFilteredAdapter by lazy { EventAdapter(this, db) }
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
        binding.profileLayout.background = null
        binding.categoriesRecycler.adapter = categoryAdapter
        binding.popularRecycler.adapter = eventAdapter
        binding.foodsByFilterRecycler.adapter = eventFilteredAdapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestPermission()
        binding.searchView.setOnQueryTextListener(this)
        with(binding) {
            profile.loadUrl(auth.currentUser?.photoUrl)
            name = auth.currentUser?.displayName

        }
        viewModel.fireBaseCategoryLiveRead()

        observe()

    }

    fun getLocation() {
        locationTask.addOnSuccessListener {
            location = LatLng(it.latitude, it.longitude)
            viewModel.fireBaseLiveRead(location)
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
                    binding.foodByCategoryLayout.visibility = View.GONE
                }

                is HomeState.Category -> {
                    binding.progressBar.visibility = View.GONE
                    binding.mainLayout.visibility = View.VISIBLE
                    binding.foodByCategoryLayout.visibility = View.GONE
                    categoryAdapter.submitList(state.categories)
                }

                is HomeState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.mainLayout.visibility = View.GONE
                    binding.foodByCategoryLayout.visibility = View.GONE
                    Toast.makeText(requireContext(), state.throwable.message, Toast.LENGTH_SHORT)
                        .show()
                }

                is HomeState.Data -> {
                    binding.progressBar.visibility = View.GONE
                    binding.mainLayout.visibility = View.VISIBLE
                    binding.foodByCategoryLayout.visibility = View.GONE
                    eventAdapter.submitList(state.events)

                }

                is HomeState.DataByFilter -> {
                    binding.progressBar.visibility = View.GONE
                    binding.mainLayout.visibility = View.GONE
                    binding.foodByCategoryLayout.visibility = View.VISIBLE
                    eventFilteredAdapter.submitList(state.events)
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

    override fun onClicked(category: String) {

        viewModel.fireBaseCategoryEventLiveRead(location, category)
    }

    override fun onClickedEvent(event: String) {
        findNavController().navigate(HomeFragmentDirections.homeToDetail(event))
    }

    override fun onQueryTextSubmit(text: String?): Boolean {
        text?.let {
            if (it.length > 3) {
                viewModel.firebaseSearchEvents(it)
            }
        }
        return true
    }

    override fun onQueryTextChange(text: String?): Boolean {
        text?.let {
            if (it.length > 3) {
                viewModel.firebaseSearchEvents( text)
            }
        }

        return true
    }


}