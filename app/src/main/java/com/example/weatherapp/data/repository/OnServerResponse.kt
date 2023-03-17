package com.example.weatherapp.data.repository

import com.example.weatherapp.data.weather_dto.WeatherDTO

fun interface OnServerResponse {
    fun onResponse (weatherDTO: WeatherDTO)
}