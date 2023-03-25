package com.example.weatherapp.data.repository

import com.example.weatherapp.domain.Repository

class RepositoryImpl : Repository {
    override fun getWeatherFromServer(): Weather {
        Thread.sleep(2000L)         // эмуляция сетевого запроса
        return Weather()                  // эмуляция ответа
    }

    override fun getWorldWeatherFromLocalStorage() = getWorldCities()

    override fun getRussianWeatherFromLocalStorage() = getRussianCities()

}