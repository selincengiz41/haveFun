package com.selincengiz.havefun.ui.user.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.selincengiz.havefun.R
import com.selincengiz.havefun.common.HomeState
import com.selincengiz.havefun.databinding.FragmentDetailBinding
import com.selincengiz.havefun.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint


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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        args.id?.let {
            viewModel.firebaseGetEvent(it)
        }

        observe()

    }

    fun observe() {
        viewModel.homeState.observe(viewLifecycleOwner) { state ->

            with(binding){


            when (state) {

                is HomeState.Detail -> {
                    progressBar2.visibility=View.GONE
                    snippet.text=state.event.title
                }

                is HomeState.Loading -> {

                    progressBar2.visibility=View.VISIBLE

                }

                is HomeState.Error -> {
                    Toast.makeText(requireContext(), state.throwable.message, Toast.LENGTH_SHORT).show()
                }

                else -> {

                }


            }
            }

        }
    }

}