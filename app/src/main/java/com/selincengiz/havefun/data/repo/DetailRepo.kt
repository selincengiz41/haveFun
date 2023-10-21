package com.selincengiz.havefun.data.repo

import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.selincengiz.havefun.common.Resource
import com.selincengiz.havefun.data.model.Address
import com.selincengiz.havefun.data.model.CommunicationInfo
import com.selincengiz.havefun.data.model.Event

class DetailRepo(private val db: FirebaseFirestore) {


    fun firebaseGetEvent(id: String, result: (Resource<Event>) -> Unit) {

        try {
            db.collection("events")
                .document(id)
                .get()
                .addOnSuccessListener { document ->


                        val address = Address(
                            document.get(FieldPath.of("adress", "country")) as String?,
                            document.get(FieldPath.of("adress", "location")) as GeoPoint?,
                            document.get(FieldPath.of("adress", "address")) as String?,

                            )

                        val info = CommunicationInfo(
                            document.get(FieldPath.of("info", "email")) as String?,
                            document.get(FieldPath.of("info", "phone")) as String?,
                        )


                        var event = Event(
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

                        result(Resource.Success(event))
                    }

                .addOnFailureListener { exception ->
                    result(Resource.Error(exception))
                }
        } catch (e: Exception) {
            result(Resource.Error(e))
        }

    }

}