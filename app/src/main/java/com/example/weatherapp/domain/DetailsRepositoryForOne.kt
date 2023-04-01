package com.example.weatherapp.domain

import com.example.weatherapp.data.repository.City
import com.example.weatherapp.ui.details.DetailsViewModel

interface DetailsRepositoryForOne {
    fun getWeatherDetails(city: City, callback: DetailsViewModel.Callback)
}