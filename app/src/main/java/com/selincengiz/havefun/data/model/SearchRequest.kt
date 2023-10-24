package com.selincengiz.havefun.data.model


import com.google.gson.annotations.SerializedName

data class SearchRequest(
    @SerializedName("searchQuery")
    val searchQuery: String
)