package com.selincengiz.havefun.data.repo

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.selincengiz.havefun.common.HomeState
import com.selincengiz.havefun.common.Resource
import com.selincengiz.havefun.data.model.Address
import com.selincengiz.havefun.data.model.CommunicationInfo
import com.selincengiz.havefun.data.model.Event

class EventRepo(private val db: FirebaseFirestore) {

    fun fireBaseEventLiveRead(location: LatLng, result:(Resource<List<Event>> )->Unit){
        try {
            // Kullanıcının konumu
            val userGeoPoint = GeoPoint(location.latitude, location.longitude)

            // 100 km'lik mesafeyi latitude ve longitude farklarına dönüştür
            val earthRadiusKm = 6371.0
            val distanceInKm = 40.0

            // Latitude ve longitude farklarını hesapla
            val latitudeDifference = (distanceInKm / earthRadiusKm) * (180.0 / Math.PI)
            val longitudeDifference =
                (distanceInKm / earthRadiusKm) * (180.0 / Math.PI) / kotlin.math.cos(location.latitude * Math.PI / 180.0)

            db.collection("events").whereGreaterThan(


                FieldPath.of("adress", "location"),
                GeoPoint(
                    userGeoPoint.latitude - latitudeDifference,
                    userGeoPoint.longitude - longitudeDifference
                )

            ).whereLessThan(

                FieldPath.of("adress", "location"),
                GeoPoint(
                    userGeoPoint.latitude + latitudeDifference,
                    userGeoPoint.longitude + longitudeDifference
                )


            )
                .addSnapshotListener { snapshot, error ->

                    val tempList = arrayListOf<Event>()

                    snapshot?.forEach { document ->

                        val address = Address(
                            document.get(FieldPath.of("adress", "country")) as String?,
                            document.get(FieldPath.of("adress", "location")) as GeoPoint?,
                            document.get(FieldPath.of("adress", "address")) as String?,

                            )

                        val info = CommunicationInfo(
                            document.get(FieldPath.of("info", "email")) as String?,
                            document.get(FieldPath.of("info", "phone")) as String?,
                        )
                        tempList.add(

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
                    error?.let {
                        result(Resource.Error(it))

                    } ?: kotlin.run {
                        result(  Resource.Success(tempList))

                    }


                }
        } catch (e: Exception) {
            result(Resource.Error(e))
        }
    }


    fun fireBaseCategoryEventLiveRead(location: LatLng, category: String, result:(Resource<List<Event>> )->Unit) {
        try {
            // Kullanıcının konumu
            val userGeoPoint = GeoPoint(location.latitude, location.longitude)

            // 100 km'lik mesafeyi latitude ve longitude farklarına dönüştür
            val earthRadiusKm = 6371.0
            val distanceInKm = 40.0

            // Latitude ve longitude farklarını hesapla
            val latitudeDifference = (distanceInKm / earthRadiusKm) * (180.0 / Math.PI)
            val longitudeDifference =
                (distanceInKm / earthRadiusKm) * (180.0 / Math.PI) / kotlin.math.cos(location.latitude * Math.PI / 180.0)

            db.collection("events").whereGreaterThan(


                FieldPath.of("adress", "location"),
                GeoPoint(
                    userGeoPoint.latitude - latitudeDifference,
                    userGeoPoint.longitude - longitudeDifference
                )

            ).whereLessThan(

                FieldPath.of("adress", "location"),
                GeoPoint(
                    userGeoPoint.latitude + latitudeDifference,
                    userGeoPoint.longitude + longitudeDifference
                )


            )
                .whereEqualTo("type", category).limit(40).addSnapshotListener { snapshot, error ->

                    val tempList = arrayListOf<Event>()

                    snapshot?.forEach { document ->

                        val address = Address(
                            document.get(FieldPath.of("adress", "country")) as String?,
                            document.get(FieldPath.of("adress", "location")) as GeoPoint?,
                            document.get(FieldPath.of("adress", "address")) as String?,

                            )

                        val info = CommunicationInfo(
                            document.get(FieldPath.of("info", "email")) as String?,
                            document.get(FieldPath.of("info", "phone")) as String?,
                        )
                        tempList.add(

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
                    error?.let {
                        result(Resource.Error(it))

                    } ?: kotlin.run {
                        result(Resource.Success(tempList))
                    }


                }
        } catch (e: Exception) {
            result(Resource.Error(e))

        }
    }



}