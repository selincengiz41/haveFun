package com.selincengiz.havefun.data.model

import com.google.gson.annotations.SerializedName

data class GetAttendedEventRequest(
    @SerializedName("attendee_email")
    val attendeeEmail: String,
)
