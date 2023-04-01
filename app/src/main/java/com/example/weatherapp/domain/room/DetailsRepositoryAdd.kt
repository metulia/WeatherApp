package com.example.weatherapp.domain.room

import com.example.weatherapp.data.repository.Weather

interface DetailsRepositoryAdd {

    fun addWeather(weather: Weather)
}