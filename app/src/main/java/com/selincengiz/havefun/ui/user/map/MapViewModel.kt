package com.selincengiz.havefun.ui.user.map

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.getField
import com.selincengiz.havefun.data.model.Address
import com.selincengiz.havefun.data.model.CommunicationInfo
import com.selincengiz.havefun.data.model.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(private val db: FirebaseFirestore) : ViewModel() {
    private var _events = MutableLiveData<List<Event>>()
    val events: MutableLiveData<List<Event>>
        get() = _events

    fun fireBaseLiveRead() {

        db.collection("events").addSnapshotListener { snapshot, error ->

            val tempList = arrayListOf<Event>()

            snapshot?.forEach { document ->

               val address = Address(
                    document.get(FieldPath.of("adress","country"))as String?,
                    document.get(FieldPath.of("adress","latitude")) as Double?,
                    document.get(FieldPath.of("adress","longitude")) as Double?,
                    document.get(FieldPath.of("adress","address")) as String?,

                    )

                val info = CommunicationInfo(
                    document.get(FieldPath.of("info","email")) as String?,
                    document.get(FieldPath.of("info","phone")) as String?,
                )
                tempList.add(
                //    document.toObject(Event::class.java)
                Event(
                        document.id,
                        document.get("title") as String?,
                        document.get("date") as String?,
                        document.get("type") as String?,
                        document.get("personLimit") as Long?,
                        document.get("locationTitle") as String?,
                        address,
                        info,
                        document.get("price") as Double?,
                    )
                )

            }
            println(tempList)
            events.value = tempList

        }
    }
}