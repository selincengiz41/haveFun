package com.selincengiz.havefun.data.model


import com.google.gson.annotations.SerializedName

data class GetCategoryResponse(
    @SerializedName("count")
    val count: Int,
    @SerializedName("data")
    val data: List<Data>,
    @SerializedName("type")
    val type: String
)