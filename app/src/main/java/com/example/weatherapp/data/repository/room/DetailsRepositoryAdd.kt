package com.example.weatherapp.data.repository.room

import com.example.weatherapp.data.repository.Weather

interface DetailsRepositoryAdd {

    fun addWeather(weather: Weather)
}