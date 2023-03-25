package com.example.weatherapp.domain

import com.example.weatherapp.data.repository.Weather

interface OnItemListClickListener {
    fun onItemClick(weather: Weather)
}