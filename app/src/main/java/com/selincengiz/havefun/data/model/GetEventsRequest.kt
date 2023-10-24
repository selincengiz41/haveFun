package com.selincengiz.havefun.data.model


import com.google.gson.annotations.SerializedName

data class GetEventsRequest(
    @SerializedName("locationLat")
    val locationLat: Double,
    @SerializedName("locationLong")
    val locationLong: Double
)