package com.example.weatherapp.domain.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WeatherHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val city: String,
    val temperature: Int,
    val feelsLike: Int,
    val icon: String
)