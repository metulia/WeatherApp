package com.example.weatherapp.data.repository.retrofit

import android.util.Log
import com.example.weatherapp.BuildConfig
import com.example.weatherapp.data.repository.City
import com.example.weatherapp.data.repository.Weather
import com.example.weatherapp.data.repository.getDefaultCity
import com.example.weatherapp.data.weatherdto.Fact
import com.example.weatherapp.data.weatherdto.WeatherDTO
import com.example.weatherapp.domain.DetailsRepositoryForOne
import com.example.weatherapp.ui.details.DetailsFragment.Companion.YANDEX_DOMAIN
import com.example.weatherapp.ui.details.DetailsViewModel
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun convertDtoToModel(weatherDTO: WeatherDTO): Weather {
    val fact: Fact = weatherDTO.fact
    return Weather(getDefaultCity(), fact.temp, fact.feels_like, fact.icon)
}

class DetailsRepositoryRetrofit2Impl : DetailsRepositoryForOne {
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
                            val weather = convertDtoToModel(it)
                            weather.city = city
                            callbackMy.onResponse(weather)
                        }
                    }
                }

                override fun onFailure(call: Call<WeatherDTO>, t: Throwable) {
                    callbackMy.onFail(Throwable(t.message ?: "REQUEST_ERROR"))
                    Log.d("@@@", "Error" + t.message)
                }

            })
    }
}