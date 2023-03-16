package com.example.weatherapp.data.repository

data class WeatherDTO(
    val factDTO: FactDTO,
    val forecastDTO: ForecastDTO,
    val infoDTO: InfoDTO,
    val now: Int,
    val now_dt: String
)