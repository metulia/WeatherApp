package com.example.weatherapp.data.repository

import com.example.weatherapp.data.repository.Repository
import com.example.weatherapp.domain.Weather
import com.example.weatherapp.domain.getRussianCities
import com.example.weatherapp.domain.getWorldCities

class RepositoryImpl : Repository {
    override fun getWeatherFromServer(): Weather {
        Thread.sleep(2000L)         // эмуляция сетевого запроса
        return Weather()                  // эмуляция ответа
    }

    override fun getWorldWeatherFromLocalStorage(): List<Weather> {
        Thread.sleep(200L)      // эмуляция запроса локального
        return getWorldCities()
    }

    override fun getRussianWeatherFromLocalStorage(): List<Weather> {
        Thread.sleep(200L)      // эмуляция запроса локального
        return getRussianCities()
    }

}