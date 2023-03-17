package com.example.weatherapp.data.repository

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.weatherapp.data.weather_dto.WeatherDTO
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class WeatherLoader(private val onServerResponseListener: OnServerResponse) {

    fun loadWeather(lat: Double, lon: Double) {

        val urlText = "https://api.weather.yandex.ru/v2/informers?lat=${lat}&lon=${lon}"
        val uri = URL(urlText)
        val urlConnection: HttpsURLConnection =
            (uri.openConnection() as HttpsURLConnection).apply {
                connectTimeout = 1000
                readTimeout = 1000
                requestMethod = "GET"
                addRequestProperty("X-Yandex-API-Key", "588bdfbd-6411-452a-b66d-830433d9a0df")
            }
        Thread {
            try {
                val headers = urlConnection.headerFields
                val responseCode = urlConnection.responseCode
                val buffer = BufferedReader(InputStreamReader(urlConnection.inputStream))
                val weatherDTO: WeatherDTO = Gson().fromJson(buffer, WeatherDTO::class.java)
                Handler(Looper.getMainLooper()).post {
                    onServerResponseListener.onResponse(
                        weatherDTO
                    )
                }
            } catch (e: JsonSyntaxException) {
                Log.d("mylogs", "Ошибка")
            } finally {
                urlConnection.disconnect()
            }
        }.start()
    }
}