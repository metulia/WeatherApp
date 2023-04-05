package com.example.weatherapp.ui.main

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.example.weatherapp.MyApp
import com.example.weatherapp.R
import com.example.weatherapp.ui.experiments.APBroadcastReceiver
import com.example.weatherapp.ui.experiments.WorkWithContentProviderFragment
import com.example.weatherapp.ui.googlemaps.MapsFragment
import com.example.weatherapp.ui.weatherhistorylist.WeatherHistoryListFragment
import com.example.weatherapp.ui.weatherlist.WeatherListFragment

class MainActivity : AppCompatActivity() {

    private val receiverAP = APBroadcastReceiver()

    private fun push() {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationBuilderLow = NotificationCompat.Builder(this, CHANNEL_ID_LOW).apply {
            setSmallIcon(R.drawable.ic_world)
            setContentTitle(getString(R.string.notification_low_title))
            setContentText(getString(R.string.notification_low_text))
            priority = NotificationManager.IMPORTANCE_LOW
        }
        val notificationBuilderHigh = NotificationCompat.Builder(this, CHANNEL_ID_HIGH).apply {
            setSmallIcon(R.drawable.ic_rus)
            setContentTitle(getString(R.string.notification_high_title))
            setContentText(getString(R.string.notification_high_text))
            priority = NotificationManager.IMPORTANCE_HIGH
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelNameLow = "Name $CHANNEL_ID_LOW"
            val channelDescriptionLow = "Description $CHANNEL_ID_LOW"
            val channelPriorityLow = NotificationManager.IMPORTANCE_LOW
            val channelLow =
                NotificationChannel(CHANNEL_ID_LOW, channelNameLow, channelPriorityLow).apply {
                    description = channelDescriptionLow
                }
            notificationManager.createNotificationChannel(channelLow)
        }
        notificationManager.notify(NOTIFICATION_ID_LOW, notificationBuilderLow.build())

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelNameHigh = "Name $CHANNEL_ID_HIGH"
            val channelDescriptionHigh = "Description $CHANNEL_ID_HIGH"
            val channelPriorityHigh = NotificationManager.IMPORTANCE_HIGH
            val channelHigh =
                NotificationChannel(CHANNEL_ID_HIGH, channelNameHigh, channelPriorityHigh).apply {
                    description = channelDescriptionHigh
                }
            notificationManager.createNotificationChannel(channelHigh)
        }
        notificationManager.notify(NOTIFICATION_ID_HIGH, notificationBuilderHigh.build())

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, WeatherListFragment.newInstance())
                .commit()
        }

        registerReceiver(receiverAP, IntentFilter("android.intent.action.AIRPLANE_MODE"))

        MyApp.getWeatherHistoryDao().getAll()

        val shP = getSharedPreferences(KEY_SHP_FILE_LIST_OF_CITIES, Context.MODE_PRIVATE)
        val editor = shP.edit()
        editor.putBoolean(KEY_SHP_FILE_LIST_OF_CITIES_KEY_IS_RUSSIAN, true).apply()

        val defaultValueIsRussian = true
        shP.getBoolean(KEY_SHP_FILE_LIST_OF_CITIES_KEY_IS_RUSSIAN, defaultValueIsRussian)

        push()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_history -> {
                supportFragmentManager.beginTransaction()
                    .add(R.id.container, WeatherHistoryListFragment.newInstance())
                    .addToBackStack("").commit()
            }
            R.id.action_work_content_provider -> {
                supportFragmentManager.beginTransaction()
                    .add(R.id.container, WorkWithContentProviderFragment.newInstance())
                    .addToBackStack("").commit()
            }
            R.id.action_google_maps -> {
                supportFragmentManager.beginTransaction()
                    .add(R.id.container, MapsFragment())
                    .addToBackStack("").commit()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        unregisterReceiver(receiverAP)
        super.onDestroy()
    }

    companion object {
        const val KEY_SHP_FILE_LIST_OF_CITIES = "list_of_cities"
        const val KEY_SHP_FILE_LIST_OF_CITIES_KEY_IS_RUSSIAN = "is_Russian"
        private const val NOTIFICATION_ID_LOW = 1
        private const val NOTIFICATION_ID_HIGH = 2
        private const val CHANNEL_ID_LOW = "channel_id_low"
        private const val CHANNEL_ID_HIGH = "channel_id_high"
    }
}