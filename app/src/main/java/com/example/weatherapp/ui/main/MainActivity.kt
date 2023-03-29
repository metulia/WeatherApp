package com.example.weatherapp.ui.main

import android.content.Context
import android.content.IntentFilter
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherapp.MyApp
import com.example.weatherapp.R
import com.example.weatherapp.ui.experiments.APBroadcastReceiver
import com.example.weatherapp.ui.weatherhistorylist.WeatherHistoryListFragment
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

        val shP = getSharedPreferences(KEY_SHP_FILE_LIST_OF_CITIES, Context.MODE_PRIVATE)
        val editor = shP.edit()
        editor.putBoolean(KEY_SHP_FILE_LIST_OF_CITIES_KEY_IS_RUSSIAN, true).apply()

        val defaultValueIsRussian = true
        shP.getBoolean(KEY_SHP_FILE_LIST_OF_CITIES_KEY_IS_RUSSIAN, defaultValueIsRussian)
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
    }
}