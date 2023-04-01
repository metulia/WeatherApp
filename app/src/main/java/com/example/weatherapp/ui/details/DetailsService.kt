package com.example.weatherapp.ui.details

import android.app.IntentService
import android.content.Intent
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.weatherapp.BuildConfig
import com.example.weatherapp.data.weatherdto.WeatherDTO
import com.example.weatherapp.ui.details.DetailsFragment.Companion.KEY_EXTRA_LAT
import com.example.weatherapp.ui.details.DetailsFragment.Companion.KEY_EXTRA_LON
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

const val KEY_BUNDLE_SERVICE_BROADCAST_WEATHER = "weather_s_b"
const val KEY_WEATHER_WAVE = "weather_wave"

class DetailsService(val name: String = "DetailService") : IntentService(name) {
    override fun onHandleIntent(intent: Intent?) {
        intent?.let {
            val lat = it.getDoubleExtra(KEY_EXTRA_LAT, 0.0)
            val lon = it.getDoubleExtra(KEY_EXTRA_LON, 0.0)
            Log.d("@@@", "work DetailsService $lat $lon")

            val urlText = "$YANDEX_DOMAIN${YANDEX_ENDPOINT}lat=${lat}&lon=${lon}"
            val uri = URL(urlText)
            val urlConnection: HttpsURLConnection =
                (uri.openConnection() as HttpsURLConnection).apply {
                    connectTimeout = 1000
                    readTimeout = 1000
                    requestMethod = "GET"
                    addRequestProperty(YANDEX_API_KEY, BuildConfig.WEATHER_API_KEY)
                }
            try {
                val headers = urlConnection.headerFields
                val responseCode = urlConnection.responseCode
                val buffer = BufferedReader(InputStreamReader(urlConnection.inputStream))
                val weatherDTO: WeatherDTO = Gson().fromJson(buffer, WeatherDTO::class.java)
                val message = Intent(KEY_WEATHER_WAVE)
                message.putExtra(KEY_BUNDLE_SERVICE_BROADCAST_WEATHER, weatherDTO)
                LocalBroadcastManager.getInstance(this).sendBroadcast(message)
            } catch (e: JsonSyntaxException) {
                Log.d("mylogs", "Ошибка JsonSyntax")

            } catch (e: FileNotFoundException) {
                Log.d("mylogs", "Ошибка FileNotFound")
            } finally {
                urlConnection.disconnect()
            }
        }
    }
}