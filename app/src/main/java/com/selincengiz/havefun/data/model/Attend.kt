package com.selincengiz.havefun.data.model

import com.google.gson.annotations.SerializedName

data class Attend(
    @SerializedName("isAttended")
    val isAttended: Int?,
)
