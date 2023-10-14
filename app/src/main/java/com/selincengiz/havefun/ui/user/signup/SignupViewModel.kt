package com.selincengiz.havefun.ui.user.signup

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.selincengiz.havefun.common.HomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(private val auth: FirebaseAuth) : ViewModel() {



    private var _homeState = MutableLiveData<HomeState>()
    val homeState: LiveData<HomeState>
        get() = _homeState

    fun firebaseSignUp(email: String, password: String, name: String, imageUri: Uri) {

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener() { task ->
                task.addOnSuccessListener {
                    firebaseUserProfileChange(name, imageUri)
                }
                task.addOnFailureListener {
                    _homeState.value =HomeState.Error(it)

                }

            }
    }


    fun firebaseUserProfileChange(name: String, imageUri: Uri) {
        val profileUpdates = userProfileChangeRequest {
            displayName = name
            photoUri = imageUri
        }
        auth.currentUser?.updateProfile(profileUpdates)
            ?.addOnCompleteListener { task ->

                task.addOnSuccessListener {
                    _homeState.value =HomeState.SignUp("Successfully signed up.")

                }
                task.addOnFailureListener {
                    _homeState.value =HomeState.Error(it)
                }

            }
    }
}