package com.selincengiz.havefun.ui.user.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.selincengiz.havefun.common.HomeState
import com.selincengiz.havefun.common.Resource
import com.selincengiz.havefun.data.model.GetEventByIdRequest
import com.selincengiz.havefun.data.model.GetEventCapacityRequest
import com.selincengiz.havefun.data.repo.ApiEventRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val apiEventRepo: ApiEventRepo,private val auth: FirebaseAuth) : ViewModel() {

    private var _homeState = MutableLiveData<HomeState>()
    val homeState: LiveData<HomeState>
        get() = _homeState

    fun getEventById(getEventByIdRequest: GetEventByIdRequest) {
        viewModelScope.launch {
            _homeState.value = HomeState.Loading
            val result = apiEventRepo.getEventById(getEventByIdRequest)
            when (result) {
                is Resource.Success -> {
                    _homeState.value = HomeState.ApiDetail(result.data)
                }

                is Resource.Error -> {
                    _homeState.value = HomeState.Error(result.throwable)
                }
            }
        }
    }

    fun getEventCapacity(id:Int){
        viewModelScope.launch {
            _homeState.value = HomeState.Loading
            val getEventCapacityRequest=GetEventCapacityRequest(auth.currentUser!!.email!!,id)
            val result = apiEventRepo.getEventCapacity(getEventCapacityRequest)
            when (result) {
                is Resource.Success -> {
                    _homeState.value = HomeState.Capacity(result.data)
                }

                is Resource.Error -> {
                    _homeState.value = HomeState.Error(result.throwable)
                }
            }
        }
    }

    fun participateEvent(id:Int){
        viewModelScope.launch {
            _homeState.value = HomeState.Loading
            val getEventCapacityRequest=GetEventCapacityRequest(auth.currentUser!!.email!!,id)
            val result = apiEventRepo.participateEvent(getEventCapacityRequest)
            when (result) {
                is Resource.Success -> {
                    _homeState.value = HomeState.Message(result.data)
                }

                is Resource.Error -> {
                    _homeState.value = HomeState.Error(result.throwable)
                }
            }
        }
    }

    fun unparticipateEvent(id:Int){
        viewModelScope.launch {
            _homeState.value = HomeState.Loading
            val getEventCapacityRequest=GetEventCapacityRequest(auth.currentUser!!.email!!,id)
            val result = apiEventRepo.unparticipateEvent(getEventCapacityRequest)
            when (result) {
                is Resource.Success -> {
                    _homeState.value = HomeState.Message(result.data)
                }

                is Resource.Error -> {
                    _homeState.value = HomeState.Error(result.throwable)
                }
            }
        }
    }


}