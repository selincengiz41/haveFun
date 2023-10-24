package com.selincengiz.havefun.common


sealed interface HomeState {
    object Loading : HomeState
    data class Error(val throwable: Throwable) : HomeState
    data class Login(val message: String) : HomeState
    data class SignUp(val message: String) : HomeState
    data class ApiCategory(val categories: List<com.selincengiz.havefun.data.model.ApiCategory>) :
        HomeState

    data class ApiEvents(val events: List<com.selincengiz.havefun.data.model.ApiEvents>) : HomeState
    data class ApiDetail(val event: List<com.selincengiz.havefun.data.model.ApiEvents>) : HomeState
    data class Message(val message: String) : HomeState


}