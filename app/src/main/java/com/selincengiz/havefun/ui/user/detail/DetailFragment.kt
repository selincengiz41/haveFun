package com.selincengiz.havefun.ui.user.detail

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.selincengiz.havefun.R
import com.selincengiz.havefun.common.Extensions.loadUrl
import com.selincengiz.havefun.common.HomeState
import com.selincengiz.havefun.data.model.EventCapacityResponse
import com.selincengiz.havefun.data.model.GetEventByIdRequest
import com.selincengiz.havefun.data.model.GetEventCapacityRequest
import com.selincengiz.havefun.databinding.FragmentDetailBinding
import com.selincengiz.havefun.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@AndroidEntryPoint
class DetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailBinding
    private val args by navArgs<DetailFragmentArgs>()
    private val viewModel by viewModels<DetailViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        binding.detailFunctions = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        args.id?.let {
            var getEventByIdRequest = GetEventByIdRequest(it)
            viewModel.getEventById(getEventByIdRequest)
            viewModel.getEventCapacity(it)
        }

        observe()

    }

    fun observe() {
        viewModel.homeState.observe(viewLifecycleOwner) { state ->

            with(binding) {


                when (state) {

                    is HomeState.ApiDetail -> {
                        progressBar2.visibility = View.GONE
                        detailLayout.visibility=View.VISIBLE

                        snippet.text = state.event.get(0).title

                        ivCategoryy.loadUrl(state.event.get(0).urlDetail)

                        val pattern = "dd.M.yyyy - HH:mm"
                        val dateFormat = SimpleDateFormat(
                            pattern,
                            Locale("tr", "TR")
                        ) // Türkçe ay isimleri için "tr_TR" locale kullanılıyor
                        val date: Date = dateFormat.parse(state.event.get(0).date)
                        val outputPattern = "dd MMMM yyyy - HH:mm" // Özel tarih formatı
                        val outputDateFormat = SimpleDateFormat(outputPattern, Locale("tr", "TR"))
                        val formattedDate = outputDateFormat.format(date)

                        val result = formattedDate.split("-")

                        tvDateDetail.text = result.get(0)
                        tvHourDetail.text = result.get(1)

                        tvAddressTitle.text = state.event.get(0).locationTitle
                        tvAddress.text = state.event.get(0).address

                        tvEmailDetail.text = state.event.get(0).email
                        tvPhoneDetail.text = state.event.get(0).phone
                    }

                    is HomeState.Loading -> {
                        detailLayout.visibility=View.GONE
                        progressBar2.visibility = View.VISIBLE

                    }

                    is HomeState.Error -> {
                        Toast.makeText(
                            requireContext(),
                            state.throwable.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    is HomeState.Capacity -> {
                        progressBar2.visibility = View.GONE
                        detailLayout.visibility=View.VISIBLE
                        if (state.data.second[0].isAttended == 1) {
                            textView2.text = "Click to cancel"
                            color = Color.parseColor("#CED2FF")
                            buttonParticipate.isClickable = true

                        } else {
                            if (state.data.first[0].personLimit!! > state.data.first[0].attendeesCount!!) {
                                textView2.text = "Participate Event"
                                color = Color.parseColor("#3D56F0")
                                buttonParticipate.isClickable = true

                            } else {
                                textView2.text = "Event is Full"
                                color = Color.parseColor("#3F4047")
                                buttonParticipate.isClickable = false
                            }


                        }
                    }

                    is HomeState.Message -> {
                        progressBar2.visibility = View.GONE
                        detailLayout.visibility=View.VISIBLE
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    }

                    else -> {

                    }


                }
            }

        }
    }

    fun participateClicked() {
        with(binding) {
            if (color == Color.parseColor("#CED2FF")) {
                viewModel.unparticipateEvent(args.id)

            } else if(color == Color.parseColor("#3D56F0")) {
                viewModel.participateEvent(args.id)
            }

            viewModel.getEventCapacity(args.id)

        }

    }

    fun backClicked() {
        findNavController().navigateUp()
    }

}