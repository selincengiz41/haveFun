package com.selincengiz.havefun.ui.user.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.selincengiz.havefun.common.HomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val auth: FirebaseAuth) : ViewModel() {

    private var _homeState = MutableLiveData<HomeState>()
    val homeState: LiveData<HomeState>
        get() = _homeState

    fun firebaseLogin(email: String, password: String) {

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener() { task ->
                task.addOnSuccessListener {
                    _homeState.value = HomeState.Login("Successfully Logined")

                }

                task.addOnFailureListener {
                    _homeState.value = HomeState.Error(it)
                }

            }
    }

}