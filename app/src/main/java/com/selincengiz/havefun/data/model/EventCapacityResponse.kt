package com.selincengiz.havefun.data.model

import com.google.gson.annotations.SerializedName

data class EventCapacityResponse(
    @SerializedName("data")
    val data: List<Capacity>,
    @SerializedName("type")
    val type: String,
    @SerializedName("attend")
val attend: List<Attend>,
)