package com.selincengiz.havefun.ui.user.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.selincengiz.havefun.common.HomeState
import com.selincengiz.havefun.common.Resource
import com.selincengiz.havefun.data.model.GetEventsByCategoriesRequest
import com.selincengiz.havefun.data.model.GetEventsRequest
import com.selincengiz.havefun.data.model.SearchRequest
import com.selincengiz.havefun.data.repo.ApiEventRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MapViewModel @Inject constructor(
    private val apiEventRepo: ApiEventRepo
) : ViewModel() {

    private var _homeState = MutableLiveData<HomeState>()
    val homeState: LiveData<HomeState>
        get() = _homeState


    fun search(searchRequest: SearchRequest) {
        viewModelScope.launch {
            _homeState.value = HomeState.Loading
            val result = apiEventRepo.search(searchRequest)
            when (result) {
                is Resource.Success -> {
                    _homeState.value = HomeState.ApiEvents(result.data)
                }

                is Resource.Error -> {
                    _homeState.value = HomeState.Error(result.throwable)
                }
            }
        }
    }

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

    fun getEvents(getEventsRequest: GetEventsRequest) {
        viewModelScope.launch {
            _homeState.value = HomeState.Loading
            val result = apiEventRepo.getEvents(getEventsRequest)
            when (result) {
                is Resource.Success -> {
                    _homeState.value = HomeState.ApiEvents(result.data)
                }

                is Resource.Error -> {
                    _homeState.value = HomeState.Error(result.throwable)
                }
            }
        }
    }

    fun getEventsByCategories(getEventsByCategoriesRequest: GetEventsByCategoriesRequest) {
        viewModelScope.launch {
            _homeState.value = HomeState.Loading
            val result = apiEventRepo.getEventsByCategories(getEventsByCategoriesRequest)
            when (result) {
                is Resource.Success -> {
                    _homeState.value = HomeState.ApiEvents(result.data)
                }

                is Resource.Error -> {
                    _homeState.value = HomeState.Error(result.throwable)
                }
            }
        }
    }
}