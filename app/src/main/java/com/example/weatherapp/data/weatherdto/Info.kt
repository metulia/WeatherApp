package com.example.weatherapp.data.weatherdto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Info(
    val lat: Double,
    val lon: Double,
    val url: String
):Parcelable