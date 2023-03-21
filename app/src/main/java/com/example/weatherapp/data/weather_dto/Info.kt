package com.example.weatherapp.data.weather_dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Info(
    val lat: Double,
    val lon: Double,
    val url: String
):Parcelable