package com.example.weatherapp.data.repository

import android.util.Log
import android.view.View
import com.example.weatherapp.BuildConfig
import com.example.weatherapp.data.weather_dto.Fact
import com.example.weatherapp.data.weather_dto.WeatherDTO
import com.example.weatherapp.domain.DetailsRepository
import com.example.weatherapp.ui.details.DetailsViewModel
import com.example.weatherapp.ui.details.YANDEX_DOMAIN
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun convertDtoToModel(weatherDTO: WeatherDTO): Weather {
    val fact: Fact = weatherDTO.fact
    return Weather(getDefaultCity(), fact.temp, fact.feels_like)
}

class DetailsRepositoryRetrofit2Impl : DetailsRepository {
    override fun getWeatherDetails(city: City, callbackMy: DetailsViewModel.Callback) {
        val weatherAPI = Retrofit.Builder().apply {
            baseUrl(YANDEX_DOMAIN)
            addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
        }.build().create(WeatherAPI::class.java)

        weatherAPI.getWeather(BuildConfig.WEATHER_API_KEY, city.lat, city.lon)
            .enqueue(object : Callback<WeatherDTO> {
                override fun onResponse(call: Call<WeatherDTO>, response: Response<WeatherDTO>) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            callbackMy.onResponse(convertDtoToModel(it))
                        }
                    }
                }

                override fun onFailure(call: Call<WeatherDTO>, t: Throwable) {
                    Log.d("@@@", "Error" + t.message)
                }

            })
    }
}