package com.selincengiz.havefun.ui.user.map

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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

import com.selincengiz.havefun.common.HomeState
import com.selincengiz.havefun.common.PermissionUtils
import com.selincengiz.havefun.common.PermissionUtils.checkPermission
import com.selincengiz.havefun.common.PermissionUtils.shouldShowRationale
import com.selincengiz.havefun.databinding.FragmentMapBinding
import dagger.hilt.android.AndroidEntryPoint
import java.net.URL


@AndroidEntryPoint
class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentMapBinding

    //private val args by navArgs<MapFragmentArgs>()
    private val viewModel by viewModels<MapViewModel>()
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false)
        flpc = LocationServices.getFusedLocationProviderClient(requireActivity())
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

                is HomeState.Data -> {
                    state.events.forEach {event ->


                        val selectedLocation = com.google.android.gms.maps.model.LatLng(
                            event.adress!!.location!!.latitude!!,
                            event.adress!!.location!!.longitude!!
                        )


                        viewModel.categoryFirebase(event.type, success =
                        {bmp->
                            try {
                                bmp?.let {

                                    Glide.with(this)
                                        .asBitmap()
                                        .load(it)
                                        .into(object : CustomTarget<Bitmap>(100,100){
                                            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                                                val marker=MarkerOptions().position(selectedLocation).title(event.title)
                                                    .snippet(event.id).icon(BitmapDescriptorFactory.fromBitmap(resource))

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


                            }catch (e:Exception){
                                Log.i("probblme",e.message.orEmpty())
                            }

                        }, fail = {
                            val marker=MarkerOptions().position(selectedLocation).title(event.title)
                                .snippet(event.id)

                            map.addMarker(
                                marker
                            )
                        } , error = {
                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        })



                    }
                }

                else -> {

                }
            }

        }


        map.setOnInfoWindowLongClickListener {
            findNavController().navigate(MapFragmentDirections.mapToDetail(it.snippet))
        }

    }

    fun getLocation() {
        locationTask.addOnSuccessListener {
            location = LatLng(it.latitude, it.longitude)
            viewModel.fireBaseLiveRead(location)
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


}