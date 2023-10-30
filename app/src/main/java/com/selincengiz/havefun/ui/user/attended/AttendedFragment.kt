package com.selincengiz.havefun.ui.user.attended

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.selincengiz.havefun.R
import com.selincengiz.havefun.common.HomeState
import com.selincengiz.havefun.data.model.GetAttendedEventRequest
import com.selincengiz.havefun.databinding.FragmentAttendedBinding
import com.selincengiz.havefun.databinding.FragmentHomeBinding
import com.selincengiz.havefun.ui.adapter.EventAdapter
import com.selincengiz.havefun.ui.adapter.ItemEventListener
import com.selincengiz.havefun.ui.user.home.HomeFragmentDirections
import com.selincengiz.havefun.ui.user.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AttendedFragment : Fragment(), ItemEventListener {
    private lateinit var binding: FragmentAttendedBinding
    private val viewModel by viewModels<AttendedViewModel>()
    private val eventAdapter by lazy { EventAdapter(this) }
    @Inject
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       binding= DataBindingUtil.inflate(inflater,R.layout.fragment_attended, container, false)
        binding.eventRecycler.adapter = eventAdapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val getAttendedEventRequest=GetAttendedEventRequest(auth.currentUser!!.email!!)
        viewModel.getAttendedEvents(getAttendedEventRequest)

        observe()
    }

    fun observe()
    {
    viewModel.homeState.observe(viewLifecycleOwner){state->

        when(state){

            is HomeState.ApiEvents->{
                binding.eventRecycler.visibility=View.VISIBLE
                eventAdapter.submitList(state.events)
            }
            is HomeState.Loading->{
                binding.eventRecycler.visibility=View.GONE
            }
            is HomeState.Error->{
                Toast.makeText(requireContext(), state.throwable.message, Toast.LENGTH_SHORT).show()
            }
            else->{

            }
        }
    }
    }

    override fun onClickedEvent(event: Int) {
        findNavController().navigate(AttendedFragmentDirections.attendedToDetail(event))
    }

}