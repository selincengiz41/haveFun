package com.selincengiz.havefun.ui.user.map

import android.annotation.SuppressLint
import android.app.Activity
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.adevinta.leku.LocationPicker
import com.adevinta.leku.LocationPickerActivity
import com.adevinta.leku.locale.SearchZoneRect
import com.adevinta.leku.tracker.LocationPickerTracker
import com.adevinta.leku.tracker.TrackEvents
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import com.selincengiz.havefun.R
import com.selincengiz.havefun.common.PermissionUtils
import com.selincengiz.havefun.common.PermissionUtils.checkPermission
import com.selincengiz.havefun.common.PermissionUtils.shouldShowRationale
import com.selincengiz.havefun.databinding.FragmentMapBinding
import com.selincengiz.havefun.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint

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
        viewModel.fireBaseLiveRead()

    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(map: GoogleMap) {

        map.isMyLocationEnabled = true
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))

        viewModel.events.observe(viewLifecycleOwner) {
            it.forEach {
                val selectedLocation = com.google.android.gms.maps.model.LatLng(
                    it.adress!!.latitude!!,
                    it.adress!!.longitude!!
                )
                map.addMarker(MarkerOptions().position(selectedLocation).title(it.title))

            }

        }
    }

    fun getLocation() {
        locationTask.addOnSuccessListener {
            location = LatLng(it.latitude, it.longitude)
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