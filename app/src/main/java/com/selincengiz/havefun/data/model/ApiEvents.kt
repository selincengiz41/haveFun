package com.selincengiz.havefun.data.model


import com.google.gson.annotations.SerializedName

data class ApiEvents(
    @SerializedName("address")
    val address: String,
    @SerializedName("categoryId")
    val categoryId: Int,
    @SerializedName("country")
    val country: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("locationLat")
    val locationLat: Double,
    @SerializedName("locationLong")
    val locationLong: Double,
    @SerializedName("locationTitle")
    val locationTitle: String,
    @SerializedName("personLimit")
    val personLimit: Int,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("price")
    val price: Double?,
    @SerializedName("title")
    val title: String
)