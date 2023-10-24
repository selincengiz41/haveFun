package com.selincengiz.havefun.ui.user.discover

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.selincengiz.havefun.common.HomeState
import com.selincengiz.havefun.common.Resource
import com.selincengiz.havefun.data.repo.ApiEventRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor(private val apiEventRepo: ApiEventRepo) : ViewModel() {

    private var _homeState = MutableLiveData<HomeState>()
    val homeState: LiveData<HomeState>
        get() = _homeState


}