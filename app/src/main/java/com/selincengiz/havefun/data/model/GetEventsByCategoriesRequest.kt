package com.selincengiz.havefun.data.model


import com.google.gson.annotations.SerializedName

data class GetEventsByCategoriesRequest(
    @SerializedName("categoryId")
    val categoryId: Int,
    @SerializedName("locationLat")
    val locationLat: Double,
    @SerializedName("locationLong")
    val locationLong: Double
)