package com.selincengiz.havefun.ui.user.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.selincengiz.havefun.common.HomeState
import com.selincengiz.havefun.common.Resource
import com.selincengiz.havefun.data.model.Event
import com.selincengiz.havefun.data.repo.DetailRepo
import com.selincengiz.havefun.ui.user.home.HomeViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val repo :DetailRepo):  ViewModel() {

    private var _homeState = MutableLiveData<HomeState>()
    val homeState: LiveData<HomeState>
        get() = _homeState


    fun firebaseGetEvent(id: String) {
        _homeState.value=HomeState.Loading
        repo.firebaseGetEvent(id){result->
            when(result){

                is Resource.Success ->
                {
                    _homeState.value= HomeState.Detail(result.data)
                }

                is Resource.Error ->{
                    _homeState.value= HomeState.Error(result.throwable)
                }
            }
        }
    }

}