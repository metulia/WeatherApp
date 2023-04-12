package com.example.weatherapp.ui.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.data.repository.City
import com.example.weatherapp.data.repository.DetailsRepositoryRoomImpl
import com.example.weatherapp.data.repository.Weather
import com.example.weatherapp.data.repository.retrofit.DetailsRepositoryRetrofit2Impl
import com.example.weatherapp.data.repository.room.DetailsRepositoryAdd
import com.example.weatherapp.domain.DetailsRepositoryForOne

open class DetailsViewModel
    (
    private val liveData: MutableLiveData<DetailsFragmentRequestResult> = MutableLiveData(),
    private val repositoryImpl: DetailsRepositoryForOne = DetailsRepositoryRetrofit2Impl(),
    private val repositoryAdd: DetailsRepositoryAdd = DetailsRepositoryRoomImpl()
) :
    ViewModel() {
    fun getLiveData() = liveData

    fun getWeather(city: City) {
        liveData.postValue(DetailsFragmentRequestResult.Loading)
        repositoryImpl.getWeatherDetails(
            city,
            object : Callback {
                override fun onResponse(weather: Weather) {
                    liveData.postValue(DetailsFragmentRequestResult.Success(weather))
                    repositoryAdd.addWeather(weather)
                }

                override fun onFail(error: Throwable) {
                    liveData.postValue((DetailsFragmentRequestResult.Error(Throwable())))
                }
            })
    }

    interface Callback {
        fun onResponse(weather: Weather) {
        }
        fun onFail(error: Throwable) {
        }
    }
}