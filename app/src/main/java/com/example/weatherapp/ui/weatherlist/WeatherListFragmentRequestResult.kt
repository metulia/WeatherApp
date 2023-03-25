package com.example.weatherapp.ui.weatherlist

import com.example.weatherapp.data.repository.Weather

sealed class WeatherListFragmentRequestResult {

    object Loading : WeatherListFragmentRequestResult()
    data class Success(val weatherListData: List <Weather>) : WeatherListFragmentRequestResult() {
    }
    data class Error(val error: Throwable) : WeatherListFragmentRequestResult()
}