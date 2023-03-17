package com.example.weatherapp.data.weather_dto

data class WeatherDTO(
    val fact: Fact,
    val forecast: Forecast,
    val info: Info,
    val now: Int,
    val now_dt: String
)