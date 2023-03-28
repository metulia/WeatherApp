package com.example.weatherapp.domain

import com.example.weatherapp.ui.weatherhistorylist.WeatherHistoryViewModel

interface DetailsRepositoryForAll {
    fun getAllWeatherDetails(callback: WeatherHistoryViewModel.CallbackForAll)
}