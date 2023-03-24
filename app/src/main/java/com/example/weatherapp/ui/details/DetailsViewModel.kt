package com.example.weatherapp.ui.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.data.repository.City
import com.example.weatherapp.data.repository.DetailsRepositoryRetrofit2Impl
import com.example.weatherapp.data.repository.Weather
import com.example.weatherapp.domain.DetailsRepository

open class DetailsViewModel
    (
    private val liveData: MutableLiveData<DetailsFragmentRequestResult> = MutableLiveData(),
    private val repositoryImpl: DetailsRepository = DetailsRepositoryRetrofit2Impl()
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
                }

                override fun onFail(error: Throwable) {
                    liveData.postValue((DetailsFragmentRequestResult.Error(Throwable())))
                }
            })
    }

    interface Callback {
        fun onResponse(weather: Weather) {
        }

        fun onFail(error: Throwable)
    }

}