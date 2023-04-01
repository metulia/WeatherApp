package com.example.weatherapp.data.repository

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.weatherapp.BuildConfig
import com.example.weatherapp.data.weatherdto.WeatherDTO
import com.example.weatherapp.domain.OnServerResponse
import com.example.weatherapp.ui.details.DetailsFragment.Companion.YANDEX_API_KEY
import com.example.weatherapp.ui.details.DetailsFragment.Companion.YANDEX_DOMAIN
import com.example.weatherapp.ui.details.DetailsFragment.Companion.YANDEX_ENDPOINT
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class WeatherLoader(private val onServerResponseListener: OnServerResponse) {

    fun loadWeather(lat: Double, lon: Double) {

        val urlText = "$YANDEX_DOMAIN${YANDEX_ENDPOINT}lat=${lat}&lon=${lon}"
        val uri = URL(urlText)
        val urlConnection: HttpsURLConnection =
            (uri.openConnection() as HttpsURLConnection).apply {
                connectTimeout = 1000
                readTimeout = 1000
                requestMethod = "GET"
                addRequestProperty(YANDEX_API_KEY, BuildConfig.WEATHER_API_KEY)
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
                Log.d("mylogs", "Ошибка JsonSyntax")
            } catch (e: FileNotFoundException) {
                Log.d("mylogs", "Ошибка FileNotFound")
            } finally {
                urlConnection.disconnect()
            }
        }.start()
    }
}