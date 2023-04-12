package com.example.weatherapp.data.repository

import com.example.weatherapp.MyApp
import com.example.weatherapp.data.repository.room.DetailsRepositoryAdd
import com.example.weatherapp.data.repository.room.WeatherHistoryEntity
import com.example.weatherapp.domain.DetailsRepositoryForAll
import com.example.weatherapp.domain.DetailsRepositoryForOne
import com.example.weatherapp.ui.details.DetailsViewModel
import com.example.weatherapp.ui.weatherhistorylist.WeatherHistoryViewModel

class DetailsRepositoryRoomImpl : DetailsRepositoryForOne, DetailsRepositoryForAll,
    DetailsRepositoryAdd {

    override fun getWeatherDetails(city: City, callback: DetailsViewModel.Callback) {
        val list = convertHistoryEntityToWeather(
            MyApp.getWeatherHistoryDao().getCityWeatherHistory(city.name)
        )
        callback.onResponse(list.last())
    }

    override fun getAllWeatherDetails(callback: WeatherHistoryViewModel.CallbackForAll) {
        callback.onResponse(convertHistoryEntityToWeather(MyApp.getWeatherHistoryDao().getAll()))
    }

    override fun addWeather(weather: Weather) {
        MyApp.getWeatherHistoryDao().insert(convertWeatherToEntity(weather))
    }

    companion object {
        private fun convertHistoryEntityToWeather(entityList: List<WeatherHistoryEntity>): List<Weather> {
            return entityList.map {
                Weather(City(it.city, 0.0, 0.0), it.temperature, it.feelsLike, it.icon)
            }
        }

        private fun convertWeatherToEntity(weather: Weather): WeatherHistoryEntity {
            return WeatherHistoryEntity(
                0,
                weather.city.name,
                weather.temperature,
                weather.feelsLike,
                weather.icon
            )
        }
    }
}