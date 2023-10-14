package com.selincengiz.havefun.ui.user.signup

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.adevinta.leku.ZIPCODE
import com.selincengiz.havefun.R
import com.selincengiz.havefun.common.Extensions.loadUrl
import com.selincengiz.havefun.common.HomeState
import com.selincengiz.havefun.common.PermissionUtils

import com.selincengiz.havefun.common.PermissionUtils.checkPermission
import com.selincengiz.havefun.common.PermissionUtils.shouldShowRationale
import com.selincengiz.havefun.databinding.FragmentSignupBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupFragment : Fragment() {
    private lateinit var binding: FragmentSignupBinding
    private val viewModel by viewModels<SignupViewModel>()
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                openGalleryIntent()

            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_signup, container, false)
        binding.signUpFunctions = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeMessage()

    }

    fun observeMessage() {
        viewModel.homeState.observe(viewLifecycleOwner) { state ->

            when (state) {
                is HomeState.SignUp -> {
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT)
                        .show()
                    findNavController().navigate(SignupFragmentDirections.signupToHome())
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

    fun signUpClicked(email: String, password: String, name: String, imageUri: Uri?) {
        if (email.isNullOrEmpty().not() && password.isNullOrEmpty().not() && name.isNullOrEmpty()
                .not() && imageUri?.path.isNullOrEmpty().not()
        ) {
            if (imageUri != null) {
                viewModel.firebaseSignUp(email, password, name, imageUri)
            }
        } else {
            Toast.makeText(requireContext(), "Please fill in the blanks.", Toast.LENGTH_SHORT)
                .show()
        }

    }


    fun toLoginClicked() {
        findNavController().navigate(SignupFragmentDirections.signupToLogin())
    }

    fun imageViewClicked() {
        requireContext().checkPermission(PermissionUtils.galleryPermission,
            onGranted = {
                openGalleryIntent()
            },
            onDenied = {
                requestPermissionLauncher.launch(PermissionUtils.galleryPermission)
            }
        )

        requireActivity().shouldShowRationale(
            PermissionUtils.galleryPermission,
            onGranted = {
                Toast.makeText(
                    requireContext(),
                    R.string.permission_required,
                    Toast.LENGTH_SHORT
                ).show()

                requestPermissionLauncher.launch(
                    PermissionUtils.galleryPermission
                )
            },
        )


    }

    private val GALLERY_REQUEST_CODE = 100
    private fun openGalleryIntent() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri: Uri? = data.data
            selectedImageUri?.let {
                binding.imageUri = it
                binding.profile.loadUrl(it)

            }


        }
    }


}