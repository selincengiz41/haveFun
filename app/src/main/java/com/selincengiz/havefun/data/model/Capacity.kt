package com.selincengiz.havefun.data.model

import com.google.gson.annotations.SerializedName

data class Capacity(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("personLimit")
    val personLimit: Int?,
    @SerializedName("attendees_count")
    val attendeesCount: Int?
)
