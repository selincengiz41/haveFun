package com.selincengiz.havefun.ui.user.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.selincengiz.havefun.data.model.Address
import com.selincengiz.havefun.data.model.Category
import com.selincengiz.havefun.data.model.CommunicationInfo
import com.selincengiz.havefun.data.model.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val db: FirebaseFirestore) : ViewModel() {
    private var _categories = MutableLiveData<List<Category>>()
    val categories: MutableLiveData<List<Category>>
        get() = _categories

    fun fireBaseCategoryLiveRead() {

        db.collection("categories").addSnapshotListener { snapshot, error ->

            val tempList = arrayListOf<Category>()

            snapshot?.forEach { document ->

                tempList.add(

                    Category(
                        document.get("url") as String?,
                        document.get("text") as String?,
                    )
                )

            }

            _categories.value = tempList

        }
    }
}