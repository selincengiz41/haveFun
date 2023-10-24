package com.selincengiz.havefun.data.source.remote


import com.selincengiz.havefun.data.model.AddEventRequest
import com.selincengiz.havefun.data.model.BaseResponse
import com.selincengiz.havefun.data.model.GetCategoryResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface EventService {

    @POST("add_event.php")
    suspend fun addEvent(@Body addEventRequest: AddEventRequest): BaseResponse

    @GET("categories.php")
    suspend fun getCategories(): GetCategoryResponse

}