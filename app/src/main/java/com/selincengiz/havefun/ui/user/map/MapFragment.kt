package com.selincengiz.havefun.ui.user.map

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.selincengiz.havefun.databinding.FragmentMapBinding
import com.selincengiz.havefun.ui.adapter.CategoryAdapter
import com.selincengiz.havefun.ui.adapter.ItemCategoryListener
import dagger.hilt.android.AndroidEntryPoint
import java.net.URL


@AndroidEntryPoint
class MapFragment : Fragment(), OnMapReadyCallback, ItemCategoryListener {

    private lateinit var binding: FragmentMapBinding

    //private val args by navArgs<MapFragmentArgs>()
    private val viewModel by viewModels<MapViewModel>()
    private lateinit var flpc: FusedLocationProviderClient
    private lateinit var locationTask: Task<Location>
    private lateinit var location: com.google.android.gms.maps.model.LatLng
    private val categoryAdapter by lazy { CategoryAdapter(this) }
    private lateinit var categories:
            List<ApiCategory>

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

                        viewModel.categories?.let {
                            it.forEach {
                                if (it.categoryId!!.equals(event.categoryId)) {

                                    if (event.categoryId == 7) {
                                        val bitmapDescriptor =
                                            BitmapDescriptorFactory.defaultMarker(
                                                BitmapDescriptorFactory.HUE_ROSE
                                            )
                                        val marker =
                                            MarkerOptions().position(selectedLocation)
                                                .title(event.title)
                                                .snippet(event.id.toString()).icon(bitmapDescriptor)

                                        map.addMarker(
                                            marker
                                        )
                                    } else {
                                        Glide.with(this)
                                            .asBitmap()
                                            .load(it.url)
                                            .apply(
                                                RequestOptions.bitmapTransform(
                                                    ColorizeTransformation(Color.parseColor("#FF007F"))
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
                            }
                        }

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

    override fun onClicked(category: String) {
        if (category.equals("Custom")) {
            var getEventsRequest = GetEventsRequest(location.latitude, location.longitude)
            viewModel.getEvents(getEventsRequest)
        } else {
            categories?.let {
                it.forEach {
                    if (it.name.equals(category)) {
                        var getEventsByCategoriesRequest =
                            GetEventsByCategoriesRequest(
                                it.categoryId!!,
                                location.latitude,
                                location.longitude
                            )
                        viewModel.getEventsByCategories(getEventsByCategoriesRequest)
                    }
                }
            }

        }
    }


}