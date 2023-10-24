package com.selincengiz.havefun.data.model


import com.google.gson.annotations.SerializedName

data class BaseResponse(
    @SerializedName("msg")
    val msg: String?,
    @SerializedName("type")
    val type: String?
)