package com.example.weatherapp.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.data.repository.RepositoryImpl

class MainViewModel(
    private val liveData: MutableLiveData<AppState> = MutableLiveData(),
    private val repository: RepositoryImpl = RepositoryImpl()
) :
    ViewModel() {
    fun getData(): LiveData<AppState> {
        return liveData
    }

    fun getWeatherRussia() = getWeather(true)
    fun getWeatherWorld() = getWeather(false)

    private fun getWeather(isRussian: Boolean) {
        Thread {
            liveData.postValue(AppState.Loading)

            if ((0..10).random() > 0) {
                val answer = if (isRussian) repository.getRussianWeatherFromLocalStorage()
                else repository.getWorldWeatherFromLocalStorage()
                liveData.postValue(AppState.Success(answer))
            } else
                liveData.postValue(AppState.Error(IllegalAccessException()))
        }.start()
    }
}