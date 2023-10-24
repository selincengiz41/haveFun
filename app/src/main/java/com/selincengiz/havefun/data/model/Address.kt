package com.selincengiz.havefun.data.model

import android.os.Parcelable
import com.google.firebase.firestore.GeoPoint
import kotlinx.parcelize.Parcelize


data class Address(
    val country: String?,
    val location: GeoPoint?,
    val address: String?

)
