package com.example.weatherapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.data.repository.RepositoryImpl
import com.example.weatherapp.ui.weatherlist.WeatherListFragmentRequestResult

class MainViewModel(
    private val liveData: MutableLiveData<WeatherListFragmentRequestResult> = MutableLiveData(),
    private val repository: RepositoryImpl = RepositoryImpl()
) :
    ViewModel() {
    fun getData(): LiveData<WeatherListFragmentRequestResult> {
        return liveData
    }

    fun getWeatherRussia() = getWeather(true)
    fun getWeatherWorld() = getWeather(false)

    private fun getWeather(isRussian: Boolean) {
        Thread {
            liveData.postValue(WeatherListFragmentRequestResult.Loading)

            //if ((0..10).random() > 0) {
            if (true) {
                val answer = if (isRussian) repository.getRussianWeatherFromLocalStorage()
                else repository.getWorldWeatherFromLocalStorage()
                liveData.postValue(WeatherListFragmentRequestResult.Success(answer))
            } else
                liveData.postValue(WeatherListFragmentRequestResult.Error(IllegalAccessException()))
        }.start()
    }
}