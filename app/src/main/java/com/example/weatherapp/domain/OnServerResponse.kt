package com.example.weatherapp.domain

import com.example.weatherapp.data.weatherdto.WeatherDTO

fun interface OnServerResponse {
    fun onResponse(weatherDTO: WeatherDTO)
}