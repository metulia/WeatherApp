package com.example.weatherapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.weatherapp.R
import com.example.weatherapp.ui.details.DetailsFragment
import com.example.weatherapp.ui.weatherlist.WeatherListFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, WeatherListFragment.newInstance())
                .commit()
        }
    }
}