package com.selincengiz.havefun.ui.user.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.selincengiz.havefun.R
import com.selincengiz.havefun.common.HomeState
import com.selincengiz.havefun.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        binding.loginFunctions = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeMessage()
    }

    fun observeMessage() {
        viewModel.homeState.observe(viewLifecycleOwner) { state ->

            when (state) {

                is HomeState.Login -> {
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT)
                        .show()
                    findNavController().navigate(LoginFragmentDirections.loginToHome())
                }

                is HomeState.Error -> {
                    Toast.makeText(requireContext(), state.throwable.message, Toast.LENGTH_SHORT)
                        .show()
                }

                else -> {

                }
            }

        }

    }

    fun loginClicked(email: String, password: String) {
        if (email.isNullOrEmpty().not() && password.isNullOrEmpty().not()) {
            viewModel.firebaseLogin(email, password)
        } else {
            Toast.makeText(requireContext(), "Please fill in the blanks.", Toast.LENGTH_SHORT)
                .show()
        }
    }


    fun toSignUpClicked() {
        findNavController().navigate(LoginFragmentDirections.loginToSignUp())
    }


}