package com.selincengiz.havefun.data.model


import com.google.gson.annotations.SerializedName

data class GetEventByIdRequest(
    @SerializedName("id")
    val id: Int
)