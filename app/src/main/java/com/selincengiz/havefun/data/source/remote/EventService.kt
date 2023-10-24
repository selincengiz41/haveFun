package com.selincengiz.havefun.data.source.remote


import com.selincengiz.havefun.data.model.AddEventRequest
import com.selincengiz.havefun.data.model.BaseResponse
import com.selincengiz.havefun.data.model.GetCategoryResponse
import com.selincengiz.havefun.data.model.GetEventByIdRequest
import com.selincengiz.havefun.data.model.GetEventResponse
import com.selincengiz.havefun.data.model.GetEventsByCategoriesRequest
import com.selincengiz.havefun.data.model.GetEventsRequest
import com.selincengiz.havefun.data.model.SearchRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface EventService {

    @POST("add_event.php")
    suspend fun addEvent(@Body addEventRequest: AddEventRequest): BaseResponse

    @GET("get_categories.php")
    suspend fun getCategories(): GetCategoryResponse

    @POST("get_events.php")
    suspend fun getEvents(@Body getEventsRequest: GetEventsRequest): GetEventResponse

    @POST("get_events_by_categories.php")
    suspend fun getEventsByCategories(@Body getEventsByCategoriesRequest: GetEventsByCategoriesRequest): GetEventResponse

    @POST("get_event_by_id.php")
    suspend fun getEventById(@Body getEventByIdRequest: GetEventByIdRequest): GetEventResponse

    @POST("search.php")
    suspend fun search(@Body searchRequest: SearchRequest): GetEventResponse

}