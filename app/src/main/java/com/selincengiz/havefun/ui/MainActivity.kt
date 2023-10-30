package com.selincengiz.havefun.ui

import android.app.Activity
import android.location.Address
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.adevinta.leku.ADDRESS
import com.adevinta.leku.LATITUDE
import com.adevinta.leku.LOCATION_ADDRESS
import com.adevinta.leku.LONGITUDE
import com.adevinta.leku.TIME_ZONE_DISPLAY_NAME
import com.adevinta.leku.TIME_ZONE_ID
import com.adevinta.leku.TRANSITION_BUNDLE
import com.adevinta.leku.ZIPCODE
import com.google.firebase.auth.FirebaseAuth
import com.selincengiz.havefun.R
import com.selincengiz.havefun.common.Extensions.loadUrl
import com.selincengiz.havefun.databinding.ActivityMainBinding
import com.selincengiz.havefun.ui.user.map.MapFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(),MapFragment.BottomNavListener {
    @Inject
    lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        with(binding) {

            //Bottom Navigation Menu
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.navHost) as NavHostFragment

            NavigationUI.setupWithNavController(bottomNavigationView, navHostFragment.navController)
            navHostFragment.navController.addOnDestinationChangedListener { controller, destination, _ ->

                when (destination.id) {

                    R.id.homeFragment -> {
                        visibilityBottomNav = true


                    }

                    R.id.discoverFragment -> {
                        visibilityBottomNav = true


                    }

                    R.id.mapFragment -> {

                        visibilityBottomNav = true


                    }
                    R.id.profileFragment -> {

                        visibilityBottomNav = true


                    }
                    R.id.attendedFragment -> {

                        visibilityBottomNav = true


                    }

                    else -> {
                        visibilityBottomNav = false


                    }

                }


            }
        }
    }


    override fun bottomNavListenerEvent(event: Boolean) {
        binding.visibilityBottomNav=!event
    }


}