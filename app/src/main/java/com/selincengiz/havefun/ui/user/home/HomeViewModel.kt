package com.selincengiz.havefun.ui.user.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
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

    fun fireBaseLiveRead(location: LatLng) {

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
                .limit(10).addSnapshotListener { snapshot, error ->

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
                        _homeState.value = HomeState.Error(it)
                    } ?: kotlin.run {
                        _homeState.value = HomeState.Data(tempList)
                    }


                }
        } catch (e: Exception) {
            _homeState.value = HomeState.Error(e)
        }

    }

}