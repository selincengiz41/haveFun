package com.selincengiz.havefun.ui.user.discover

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.mindinventory.midrawer.MIDrawerView
import com.mindinventory.midrawer.MIDrawerView.Companion.MI_TYPE_DOOR_IN
import com.mindinventory.midrawer.MIDrawerView.Companion.MI_TYPE_DOOR_OUT
import com.mindinventory.midrawer.MIDrawerView.Companion.MI_TYPE_SLIDE
import com.mindinventory.midrawer.MIDrawerView.Companion.MI_TYPE_SLIDE_WITH_CONTENT
import com.selincengiz.havefun.R
import com.selincengiz.havefun.common.HomeState
import com.selincengiz.havefun.databinding.FragmentDiscoverBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DiscoverFragment : Fragment() , View.OnClickListener{


    private lateinit var binding :FragmentDiscoverBinding
    private val viewModel by viewModels<DiscoverViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_discover,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getCategories()
        observe()

        // Set color for the container's content as transparent
        binding.drawerLayout.setScrimColor(Color.TRANSPARENT)

        binding.navScroll.setOnClickListener(this)
        binding.navSlide.setOnClickListener(this)
        binding.navDoorIn.setOnClickListener(this)
        binding.navDoorOut.setOnClickListener(this)




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

    fun observe(){
        viewModel.homeState.observe(viewLifecycleOwner){state->

            when(state)
            {
                is HomeState.Deneme->{
                    state.categories.forEach {
                        Log.i("hadibakamm",it.name)
                    }

                }
                is HomeState.Error->{
                    Log.i("hadibakamm",state.throwable.message!!)
                }
                else->{

                }
            }
        }
    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.nav_scroll -> {
                avoidDoubleClicks(binding.navScroll)

            }
            R.id.nav_slide -> {
                avoidDoubleClicks(binding.navSlide)

            }
            R.id.nav_doorIn -> {
                avoidDoubleClicks(binding.navDoorIn)

            }
            R.id.nav_doorOut -> {
                avoidDoubleClicks(binding.navDoorIn)

            }
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


}