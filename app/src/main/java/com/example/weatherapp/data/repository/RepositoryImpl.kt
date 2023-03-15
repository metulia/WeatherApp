package com.example.weatherapp.data.repository

import com.example.weatherapp.domain.Repository
import com.example.weatherapp.domain.Weather
import com.example.weatherapp.domain.getRussianCities
import com.example.weatherapp.domain.getWorldCities

class RepositoryImpl : Repository {
    override fun getWeatherFromServer(): Weather {
        Thread.sleep(2000L)         // эмуляция сетевого запроса
        return Weather()                  // эмуляция ответа
    }

    override fun getWorldWeatherFromLocalStorage() = getWorldCities()

    override fun getRussianWeatherFromLocalStorage() = getRussianCities()

}