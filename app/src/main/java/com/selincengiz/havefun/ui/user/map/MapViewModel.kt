package com.selincengiz.havefun.ui.user.map

import android.R.attr.src
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.selincengiz.havefun.common.HomeState
import com.selincengiz.havefun.common.Resource
import com.selincengiz.havefun.data.model.Address
import com.selincengiz.havefun.data.model.CommunicationInfo
import com.selincengiz.havefun.data.model.Event
import com.selincengiz.havefun.data.repo.CategoryRepo
import com.selincengiz.havefun.data.repo.EventRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.Exception
import java.lang.Math.PI
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject


@HiltViewModel
class MapViewModel @Inject constructor(
    private val repo: EventRepo,
    private val categoryRepo: CategoryRepo
) : ViewModel() {

    private var _homeState = MutableLiveData<HomeState>()
    val homeState: LiveData<HomeState>
        get() = _homeState

    fun fireBaseLiveRead(location: LatLng) {
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

    fun categoryFirebase(
        category: String?,
        success: (String?) -> Unit,
        fail: () -> Unit,
        error: (Exception) -> Unit
    ) {

        categoryRepo.categoryFirebase(category, success = {
            success(it)
        }, fail = {
            fail()
        }, error = {
            error(it)
        })

    }
}