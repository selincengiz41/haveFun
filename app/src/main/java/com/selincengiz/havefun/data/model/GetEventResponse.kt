package com.selincengiz.havefun.data.model


import com.google.gson.annotations.SerializedName

data class GetEventResponse(
    @SerializedName("count")
    val count: Int,
    @SerializedName("data")
    val data: List<ApiEvents>,
    @SerializedName("type")
    val type: String
)