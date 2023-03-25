package com.example.weatherapp.data.repository

import com.example.weatherapp.data.weather_dto.WeatherDTO
import com.example.weatherapp.ui.details.DetailsFragment.Companion.KEY_EXTRA_LAT
import com.example.weatherapp.ui.details.DetailsFragment.Companion.KEY_EXTRA_LON
import com.example.weatherapp.ui.details.DetailsFragment.Companion.YANDEX_API_KEY
import com.example.weatherapp.ui.details.DetailsFragment.Companion.YANDEX_ENDPOINT
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface WeatherAPI {
    @GET(YANDEX_ENDPOINT)
    fun getWeather(
        @Header(YANDEX_API_KEY) apikey: String,
        @Query(KEY_EXTRA_LAT) lat: Double,
        @Query(KEY_EXTRA_LON) lon: Double
    ): Call<WeatherDTO>
}