package com.selincengiz.havefun.ui.event

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.selincengiz.havefun.common.HomeState
import com.selincengiz.havefun.common.Resource
import com.selincengiz.havefun.data.model.AddEventRequest
import com.selincengiz.havefun.data.repo.ApiEventRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateEventViewModel @Inject constructor(private val apiEventRepo: ApiEventRepo) :
    ViewModel() {

    private var _homeState = MutableLiveData<HomeState>()
    val homeState: LiveData<HomeState>
        get() = _homeState


    fun getCategories() {
        viewModelScope.launch {
            _homeState.value = HomeState.Loading
            val result = apiEventRepo.getCategories()
            when (result) {
                is Resource.Success -> {
                    _homeState.value = HomeState.ApiCategory(result.data)
                }

                is Resource.Error -> {
                    _homeState.value = HomeState.Error(result.throwable)
                }
            }
        }
    }

    fun addEvent(addEventRequest: AddEventRequest) {
        viewModelScope.launch {
            _homeState.value = HomeState.Loading
            val result = apiEventRepo.addEvent(addEventRequest)
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