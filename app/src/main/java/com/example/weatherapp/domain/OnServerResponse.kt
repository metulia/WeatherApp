package com.example.weatherapp.domain

import com.example.weatherapp.data.weather_dto.WeatherDTO

fun interface OnServerResponse {
    fun onResponse(weatherDTO: WeatherDTO)
}