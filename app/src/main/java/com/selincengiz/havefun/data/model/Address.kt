package com.selincengiz.havefun.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Address(
    val country:String?,
    val latitude: Double?,
    val longitude: Double?,
    val address:String?

):Parcelable
