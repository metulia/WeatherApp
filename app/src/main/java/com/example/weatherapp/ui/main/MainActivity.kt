package com.example.weatherapp.ui.main

import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherapp.MyApp
import com.example.weatherapp.R
import com.example.weatherapp.ui.experiments.APBroadcastReceiver
import com.example.weatherapp.ui.weatherlist.WeatherListFragment

class MainActivity : AppCompatActivity() {

    private val receiverAP = APBroadcastReceiver()

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
    }

    override fun onDestroy() {
        unregisterReceiver(receiverAP)
        super.onDestroy()
    }
}