package com.selincengiz.havefun.ui.user.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.selincengiz.havefun.common.HomeState
import com.selincengiz.havefun.common.Resource
import com.selincengiz.havefun.data.model.Address
import com.selincengiz.havefun.data.model.Category
import com.selincengiz.havefun.data.model.CommunicationInfo
import com.selincengiz.havefun.data.model.Event
import com.selincengiz.havefun.data.repo.CategoryRepo
import com.selincengiz.havefun.data.repo.EventRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: EventRepo,
    private val categoryRepo: CategoryRepo
) : ViewModel() {

    private var _homeState = MutableLiveData<HomeState>()
    val homeState: LiveData<HomeState>
        get() = _homeState

    fun firebaseSearchEvents(text: String) {
        _homeState.value = HomeState.Loading
        repo.firebaseSearchEvents(text) { result ->
            when (result) {
                is Resource.Success -> {
                    _homeState.value = HomeState.Data(result.data)
                }

                is Resource.Error -> {
                    _homeState.value = HomeState.Error(result.throwable)
                }
            }
        }
    }

    fun fireBaseCategoryLiveRead() {
        _homeState.value = HomeState.Loading
        categoryRepo.fireBaseCategoryLiveRead { result ->
            when (result) {
                is Resource.Success -> {
                    _homeState.value = HomeState.Category(result.data)
                }

                is Resource.Error -> {
                    _homeState.value = HomeState.Error(result.throwable)
                }
            }
        }
    }

    fun fireBaseCategoryEventLiveRead(location: LatLng, category: String) {
        _homeState.value = HomeState.Loading
        repo.fireBaseCategoryEventLiveRead(location, category) { result ->
            when (result) {
                is Resource.Success -> {
                    _homeState.value = HomeState.Data(result.data)
                }

                is Resource.Error -> {
                    _homeState.value = HomeState.Error(result.throwable)
                }
            }
        }
    }

    fun fireBaseLiveRead(location: LatLng) {
        _homeState.value = HomeState.Loading
        repo.fireBaseEventLiveRead(location) { result ->
            when (result) {
                is Resource.Success -> {
                    _homeState.value = HomeState.Data(result.data)
                }

                is Resource.Error -> {
                    _homeState.value = HomeState.Error(result.throwable)
                }
            }
        }

    }

}