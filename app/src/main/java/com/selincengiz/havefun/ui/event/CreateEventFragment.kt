package com.selincengiz.havefun.ui.event


import android.annotation.SuppressLint
import android.app.Activity
import android.location.Address
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.adevinta.leku.ADDRESS
import com.adevinta.leku.LATITUDE
import com.adevinta.leku.LOCATION_ADDRESS
import com.adevinta.leku.LONGITUDE
import com.adevinta.leku.LocationPickerActivity
import com.adevinta.leku.TIME_ZONE_DISPLAY_NAME
import com.adevinta.leku.TIME_ZONE_ID
import com.adevinta.leku.TRANSITION_BUNDLE
import com.adevinta.leku.ZIPCODE
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.GeoPoint
import com.selincengiz.havefun.R
import com.selincengiz.havefun.common.Extensions.showDatePicker
import com.selincengiz.havefun.common.Extensions.showTimePicker
import com.selincengiz.havefun.common.PermissionUtils
import com.selincengiz.havefun.common.PermissionUtils.checkPermission
import com.selincengiz.havefun.common.PermissionUtils.shouldShowRationale
import com.selincengiz.havefun.data.model.CommunicationInfo
import com.selincengiz.havefun.data.model.Event
import com.selincengiz.havefun.databinding.CustomTypeAlertBinding
import com.selincengiz.havefun.databinding.FragmentCreateEventBinding
import dagger.hilt.android.AndroidEntryPoint

import java.util.Calendar

@AndroidEntryPoint
class CreateEventFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentCreateEventBinding
    private lateinit var flpc: FusedLocationProviderClient
    private lateinit var locationTask: Task<Location>
    private var locationAdress: com.selincengiz.havefun.data.model.Address? = null
    private val viewModel by viewModels<CreateEventViewModel>()

    val lekuActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val latitude = result.data?.getDoubleExtra(LATITUDE, 0.0)

                val longitude = result.data?.getDoubleExtra(LONGITUDE, 0.0)
                val address = result.data?.getStringExtra(LOCATION_ADDRESS)
                val postalcode = result.data?.getStringExtra(ZIPCODE)
                val bundle = result.data?.getBundleExtra(TRANSITION_BUNDLE)
                val fullAddress = result.data?.getParcelableExtra<Address>(ADDRESS)

                if (fullAddress != null) {
                    Log.d("FULL ADDRESS****", fullAddress.toString())
                    val geoPoint = GeoPoint(latitude!!, longitude!!)
                    locationAdress = com.selincengiz.havefun.data.model.Address(
                        fullAddress.countryName,
                        geoPoint,
                        address
                    )

                }
                val timeZoneId = result.data?.getStringExtra(TIME_ZONE_ID)
                if (timeZoneId != null) {
                    Log.d("TIME ZONE ID****", timeZoneId)
                }
                val timeZoneDisplayName = result.data?.getStringExtra(TIME_ZONE_DISPLAY_NAME)
                if (timeZoneDisplayName != null) {
                    Log.d("TIME ZONE NAME****", timeZoneDisplayName)
                }


                binding.tvLocationInfo.text = "Adress: " + address
                binding.tvLocationInfo.isVisible = true
            } else {
                Log.d("RESULT****", "CANCELLED")
            }
        }


    @SuppressLint("MissingPermission")
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->

            if (isGranted) {

                locationTask = flpc.lastLocation
                getLocation()

            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_create_event, container, false)
        binding.createEventFunctions = this
        flpc = LocationServices.getFusedLocationProviderClient(requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        spinnerAdapter()

    }

    fun spinnerAdapter() = with(binding) {
        var selectedType = ""
        var saveTypeList =
            mutableListOf("Concert", "Cinema", "Sport", "Art", "Museum", "Theatre", "Custom")
        val saveTypeAdapter = ArrayAdapter(
            requireContext(),
            androidx.transition.R.layout.support_simple_spinner_dropdown_item,
            saveTypeList
        )
        act.setAdapter(saveTypeAdapter)

        act.setOnItemClickListener { _, _, position, _ ->
            if (saveTypeList[position] == "Custom") {
                val builder = AlertDialog.Builder(requireContext())
                val binding = CustomTypeAlertBinding.inflate(layoutInflater)
                builder.setView(binding.root)
                val dialog = builder.create()

                with(binding) {
                    ivGo.setOnClickListener {
                        saveTypeList.add(etCustom.text.toString())
                        selectedType = etCustom.text.toString()
                        dialog.dismiss()
                    }
                }

                dialog.show()

            } else {
                selectedType = saveTypeList[position]
            }
        }
    }

    fun dateClicked() = with(binding) {
        var selectedDate = ""
        val calendar = Calendar.getInstance()

        showDatePicker(calendar) { year, month, day ->
            showTimePicker(calendar) { hour, minute ->
                selectedDate = "$day.$month.$year - $hour:$minute"
                etDate.setText(selectedDate)

            }
        }

    }

    fun mapClicked() {
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

    fun saveClicked() = with(binding) {
        val title = etTitle.text.toString()
        val date = etDate.text.toString()
        val type = act.text.toString()
        val personLimit = etPerson.text.toString()
        val communicationMail = etMail.text.toString()
        val communicationPhone = etPhone.text.toString()
        val locationTitle = etLocationTitle.text.toString()
        val price = etPrice.text.toString()

        if (title.isNullOrEmpty().not() && date.isNullOrEmpty().not() && type.isNullOrEmpty()
                .not() && personLimit.isNullOrEmpty().not() && locationTitle.isNullOrEmpty()
                .not() && locationAdress != null && communicationMail.isNullOrEmpty().not() && communicationPhone.isNullOrEmpty().not()
        ) {
            val info = CommunicationInfo(communicationPhone, communicationMail)
            val event = Event(
                "",
                title,
                date,
                type,
                personLimit.toLongOrNull(),
                locationTitle,
                locationAdress,
                info,
                price.toDoubleOrNull()
            )
            //firebase
            viewModel.firebaseSave(event, success = {
                Toast.makeText(requireContext(), "Succesfully saved.", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }, fail = {
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
            })

        } else {
            Toast.makeText(requireContext(), "Please fill the required zone!", Toast.LENGTH_SHORT)
                .show()
        }

    }


    fun getLocation() {
        locationTask.addOnSuccessListener {
            it?.let {
                val locationPickerIntent = LocationPickerActivity.Builder()
                    .withLocation(it.latitude, it.longitude)
                    .withGeolocApiKey("AIzaSyBWVkgunnXT9MibCifbY80g4JCx-mOQChM")
                    .withGooglePlacesApiKey("AIzaSyBWVkgunnXT9MibCifbY80g4JCx-mOQChM")
                    .withSearchZone("tr_TR")
                    // .withSearchZone(SearchZoneRect(LatLng(26.525467, -18.910366), LatLng(43.906271, 5.394197)))
                    .withDefaultLocaleSearchZone()
                    .shouldReturnOkOnBackPressed()
                    .withZipCodeHidden()
                    .withSatelliteViewHidden()
                    .withGooglePlacesEnabled()
                    .withGoogleTimeZoneEnabled()
                    .build(requireActivity().applicationContext)


                binding.maps.getFragment<SupportMapFragment>().getMapAsync(this@CreateEventFragment)
                lekuActivityResultLauncher.launch(locationPickerIntent)

            }


        }
    }

    override fun onMapReady(p0: GoogleMap) {

    }


}