package com.selincengiz.havefun.data.model


import com.google.gson.annotations.SerializedName

data class ApiCategory(
    @SerializedName("categoryId")
    val categoryId: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("url")
    val url: String?,
    @SerializedName("urlDetail")
    val urlDetail: String?,
    @SerializedName("urlHome")
    val urlHome: String?
)