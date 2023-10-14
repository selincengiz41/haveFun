package com.selincengiz.havefun.ui.user.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.selincengiz.havefun.common.HomeState
import com.selincengiz.havefun.data.model.Address
import com.selincengiz.havefun.data.model.Category
import com.selincengiz.havefun.data.model.CommunicationInfo
import com.selincengiz.havefun.data.model.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val db: FirebaseFirestore) : ViewModel() {

    private var _homeState = MutableLiveData<HomeState>()
    val homeState: LiveData<HomeState>
        get() = _homeState


    fun fireBaseCategoryLiveRead() {
        _homeState.value=HomeState.Loading
        db.collection("categories").addSnapshotListener { snapshot, error ->

            val tempList = arrayListOf<Category>()

            snapshot?.forEach { document ->

                tempList.add(

                    Category(
                        document.get("id") as String?,
                        document.get("url") as String?,
                        document.get("text") as String?,
                    )
                )

            }
            error?.let {
                _homeState.value=HomeState.Error(it)
            }?: kotlin.run {
                _homeState.value=HomeState.Category(tempList)
            }



        }
    }
}