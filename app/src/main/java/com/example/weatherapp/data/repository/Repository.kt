package com.example.weatherapp.data.repository

import com.example.weatherapp.domain.Weather

interface Repository {

    fun getWeatherFromServer(): Weather
    fun getWeatherFromLocalStorage(): Weather
}