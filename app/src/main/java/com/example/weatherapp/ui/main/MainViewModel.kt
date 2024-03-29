package com.example.weatherapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.data.repository.RepositoryImpl
import com.example.weatherapp.ui.weatherlist.WeatherListFragmentRequestResult

class MainViewModel(
    private val liveData: MutableLiveData<WeatherListFragmentRequestResult> = MutableLiveData(),
    private val repositoryImpl: RepositoryImpl = RepositoryImpl()
) :
    ViewModel() {
    fun getData(): LiveData<WeatherListFragmentRequestResult> {
        return liveData
    }

    fun getWeatherRussia() = getWeather(true)
    fun getWeatherWorld() = getWeather(false)

    private fun getWeather(isRussian: Boolean) {
        Thread {
            with(liveData) {

                postValue(WeatherListFragmentRequestResult.Loading)

                try {
                    val answer = if (isRussian) repositoryImpl.getRussianWeatherFromLocalStorage()
                    else repositoryImpl.getWorldWeatherFromLocalStorage()
                    postValue(WeatherListFragmentRequestResult.Success(answer))
                } catch (e: java.lang.Exception) {
                    postValue(WeatherListFragmentRequestResult.Error(IllegalAccessException()))
                }
            }
        }.start()
    }
}