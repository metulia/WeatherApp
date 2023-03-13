package com.example.weatherapp.ui.weatherlist

import com.example.weatherapp.domain.Weather

interface OnItemListClickListener {
    fun onItemClick(weather: Weather)
}