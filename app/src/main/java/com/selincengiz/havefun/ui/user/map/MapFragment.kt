package com.selincengiz.havefun.ui.user.map

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import com.selincengiz.havefun.R
import com.selincengiz.havefun.common.ColorizeTransformation
import com.selincengiz.havefun.common.HomeState
import com.selincengiz.havefun.common.PermissionUtils
import com.selincengiz.havefun.common.PermissionUtils.checkPermission
import com.selincengiz.havefun.common.PermissionUtils.shouldShowRationale
import com.selincengiz.havefun.data.model.ApiCategory
import com.selincengiz.havefun.data.model.GetEventsByCategoriesRequest
import com.selincengiz.havefun.data.model.GetEventsRequest
import com.selincengiz.havefun.data.model.SearchRequest
import com.selincengiz.havefun.databinding.FragmentMapBinding
import com.selincengiz.havefun.ui.adapter.CategoryAdapter
import com.selincengiz.havefun.ui.adapter.ItemCategoryListener
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MapFragment : Fragment(), OnMapReadyCallback, ItemCategoryListener,   android.widget.SearchView.OnQueryTextListener,
    View.OnFocusChangeListener {

    private lateinit var binding: FragmentMapBinding
    //private val args by navArgs<MapFragmentArgs>()
    private val viewModel by viewModels<MapViewModel>()
    private lateinit var flpc: FusedLocationProviderClient
    private lateinit var locationTask: Task<Location>
    private lateinit var location: com.google.android.gms.maps.model.LatLng
    private val categoryAdapter by lazy { CategoryAdapter(this, "Map") }
    private lateinit var categories:
            List<ApiCategory>
    private val colors = arrayOf(
        Color.parseColor("#F0635A"),
        Color.parseColor("#F59762"),
        Color.parseColor("#29D697"),
        Color.parseColor("#46CDFB"),
        Color.parseColor("#FFCD6C"),
        Color.parseColor("#EB5757"),
        Color.parseColor("#3D56F0"),

        )

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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false)
        flpc = LocationServices.getFusedLocationProviderClient(requireActivity())
        binding.categoriesRecycler.adapter = categoryAdapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchView.setOnQueryTextListener(this)
        binding.searchView.setOnQueryTextFocusChangeListener(this)
        binding.searchView.onFocusChangeListener=this
        requestPermission()

    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(map: GoogleMap) {
        val markerList = ArrayList<Marker>()
        map.isMyLocationEnabled = true
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))

        viewModel.homeState.observe(viewLifecycleOwner) { state ->

            when (state) {
                is HomeState.Error -> {
                    Toast.makeText(requireContext(), state.throwable.message, Toast.LENGTH_SHORT)
                        .show()
                }

                is HomeState.ApiEvents -> {
                    map.clear()
                    state.events.forEach { event ->

                        val selectedLocation = com.google.android.gms.maps.model.LatLng(
                            event.locationLat,
                            event.locationLong
                        )
                        var color = 0
                        when (event.categoryId) {
                            1 -> {
                                color = colors.get(0)
                            }

                            2 -> {
                                color = colors.get(1)
                            }

                            3 -> {
                                color = colors.get(2)
                            }

                            4 -> {
                                color = colors.get(3)
                            }

                            5 -> {
                                color = colors.get(4)
                            }

                            6 -> {
                                color = colors.get(5)
                            }

                            7 -> {
                                color = colors.get(6)
                            }
                        }

                        Glide.with(this)
                            .asBitmap()
                            .load(event.url)
                            .apply(
                                RequestOptions.bitmapTransform(
                                    ColorizeTransformation(color)
                                )
                            )
                            .into(object : CustomTarget<Bitmap>(100, 100) {
                                override fun onResourceReady(
                                    resource: Bitmap,
                                    transition: Transition<in Bitmap>?
                                ) {
                                    val marker =
                                        MarkerOptions().position(selectedLocation)
                                            .title(event.title)
                                            .snippet(event.id.toString()).icon(
                                                BitmapDescriptorFactory.fromBitmap(
                                                    resource
                                                )
                                            )

                                    map.addMarker(
                                        marker
                                    )
                                }

                                override fun onLoadCleared(placeholder: Drawable?) {
                                    // this is called when imageView is cleared on lifecycle call or for
                                    // some other reason.
                                    // if you are referencing the bitmap somewhere else too other than this imageView
                                    // clear it here as you can no longer have the bitmap
                                }
                            })


                    }
                }

                is HomeState.ApiCategory -> {
                    categories = state.categories
                    categoryAdapter.submitList(state.categories)
                }


                else -> {

                }
            }

        }


        map.setOnInfoWindowLongClickListener {
            findNavController().navigate(MapFragmentDirections.mapToDetail(it.snippet!!.toInt()))
        }

        map.setOnMapClickListener {
            binding.searchView.clearFocus()
        }


    }

    fun getLocation() {
        locationTask.addOnSuccessListener {
            location = LatLng(it.latitude, it.longitude)
            viewModel.getCategories()
            var getEventsRequest = GetEventsRequest(location.latitude, location.longitude)
            viewModel.getEvents(getEventsRequest)
            binding.maps.getFragment<SupportMapFragment>().getMapAsync(this@MapFragment)
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

    override fun onFocusChange(p0: View?, p1: Boolean) {
        Log.i("focus",p1.toString())
        val listener: BottomNavListener = activity as BottomNavListener
         listener.bottomNavListenerEvent(p1)

    }

    interface BottomNavListener {
        fun bottomNavListenerEvent(event: Boolean)
    }
}