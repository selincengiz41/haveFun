package com.selincengiz.havefun.data.model


import com.google.gson.annotations.SerializedName

data class GetEventCapacityRequest(
    @SerializedName("attendee_email")
    val attendeeEmail: String,
    @SerializedName("id")
    val id: Int
)