package com.example.weatherapp.domain

import com.example.weatherapp.data.repository.Weather

interface Repository {

    fun getWeatherFromServer(): Weather
    fun getWorldWeatherFromLocalStorage(): List<Weather>
    fun getRussianWeatherFromLocalStorage(): List<Weather>
}