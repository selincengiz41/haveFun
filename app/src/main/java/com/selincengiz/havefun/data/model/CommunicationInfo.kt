package com.selincengiz.havefun.data.model

import android.os.Parcelable
import androidx.navigation.NavType
import kotlinx.parcelize.Parcelize

@Parcelize
data class CommunicationInfo(
    val phone:String?,
    val email:String?,
):Parcelable
