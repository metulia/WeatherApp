package com.example.weatherapp.data.repository

fun interface OnServerResponse {
    fun onResponse (weatherDTO: WeatherDTO)
}