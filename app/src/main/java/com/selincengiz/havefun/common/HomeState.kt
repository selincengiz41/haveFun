package com.selincengiz.havefun.common


import com.selincengiz.havefun.data.model.Event

sealed interface HomeState {
    object Loading : HomeState
    data class Data(val foods: List<Event>) : HomeState
    data class Category(val categories: List<String>) : HomeState
    data class Error(val throwable: Throwable) : HomeState
    data class DataByFilter(val foods: List<Event>) : HomeState
    data class Detail(val event: Event) : HomeState
    data class Login(val message:String):HomeState
    data class SignUp(val message:String):HomeState


}