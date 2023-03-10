package com.example.weatherapp.domain

sealed class AppState {

    object Loading : AppState()
    data class Success(val weatherListData: List <Weather>) : AppState() {
    }

    data class Error(val error: Throwable) : AppState()
}