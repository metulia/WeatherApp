package com.example.weatherapp.ui.details

import com.example.weatherapp.data.repository.Weather

sealed class DetailsFragmentRequestResult {
    object Loading : DetailsFragmentRequestResult()
    data class Success(val weather: Weather) : DetailsFragmentRequestResult()
    data class Error(val error: Throwable) : DetailsFragmentRequestResult()
}