package com.example.weatherapp.ui.weatherhistorylist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.data.repository.DetailsRepositoryRoomImpl
import com.example.weatherapp.data.repository.Weather
import com.example.weatherapp.ui.weatherlist.WeatherListFragmentRequestResult

class WeatherHistoryViewModel(
    private val liveData: MutableLiveData<WeatherListFragmentRequestResult> = MutableLiveData(),
    private val repositoryImpl: DetailsRepositoryRoomImpl = DetailsRepositoryRoomImpl()
) :
    ViewModel() {

    fun getData(): LiveData<WeatherListFragmentRequestResult> {
        return liveData
    }

    fun getAll() {

        repositoryImpl.getAllWeatherDetails(object : CallbackForAll {

            override fun onResponse(weatherList: List<Weather>) {
                liveData.postValue(WeatherListFragmentRequestResult.Success(weatherList))
            }

            override fun onFail(error: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    interface CallbackForAll {

        fun onResponse(weatherList: List<Weather>) {
        }
        fun onFail(error: Throwable) {
        }
    }
}