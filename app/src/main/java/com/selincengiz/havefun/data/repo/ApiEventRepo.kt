package com.selincengiz.havefun.data.repo

import com.selincengiz.havefun.common.Resource
import com.selincengiz.havefun.data.model.AddEventRequest
import com.selincengiz.havefun.data.model.ApiCategory
import com.selincengiz.havefun.data.model.ApiEvents
import com.selincengiz.havefun.data.model.GetEventByIdRequest
import com.selincengiz.havefun.data.model.GetEventsByCategoriesRequest
import com.selincengiz.havefun.data.model.GetEventsRequest

import com.selincengiz.havefun.data.source.remote.EventService
import java.lang.Exception

class ApiEventRepo(
    private val eventService: EventService,

    ) {

    suspend fun getCategories(): Resource<List<ApiCategory>> {

        return try {
            Resource.Success(eventService.getCategories().data.orEmpty())
        } catch (e: Exception) {

            Resource.Error(e)
        }
    }

    suspend fun addEvent(addEventRequest: AddEventRequest): Resource<String> {

        return try {
            Resource.Success(eventService.addEvent(addEventRequest).msg.orEmpty())
        } catch (e: Exception) {

            Resource.Error(e)
        }
    }

    suspend fun getEvents(getEventsRequest: GetEventsRequest): Resource<List<ApiEvents>> {

        return try {
            Resource.Success(eventService.getEvents(getEventsRequest).data.orEmpty())
        } catch (e: Exception) {

            Resource.Error(e)
        }
    }

    suspend fun getEventsByCategories(getEventsByCategoriesRequest: GetEventsByCategoriesRequest): Resource<List<ApiEvents>> {

        return try {
            Resource.Success(eventService.getEventsByCategories(getEventsByCategoriesRequest).data.orEmpty())
        } catch (e: Exception) {

            Resource.Error(e)
        }
    }

    suspend fun getEventById(getEventByIdRequest: GetEventByIdRequest): Resource<List<ApiEvents>> {

        return try {
            Resource.Success(eventService.getEventById(getEventByIdRequest).data.orEmpty())
        } catch (e: Exception) {

            Resource.Error(e)
        }
    }


}