package com.selincengiz.havefun.data.repo

import com.google.firebase.auth.FirebaseAuth
import com.selincengiz.havefun.common.Resource
import com.selincengiz.havefun.data.model.Data

import com.selincengiz.havefun.data.source.remote.EventService
import java.lang.Exception

class FoodRepo(
    private val eventService: EventService,

) {

    suspend fun getCategories(): Resource<List<Data>> {

        return try {
            Resource.Success(eventService.getCategories().data.orEmpty())
        } catch (e: Exception) {

            Resource.Error(e)
        }
    }








}