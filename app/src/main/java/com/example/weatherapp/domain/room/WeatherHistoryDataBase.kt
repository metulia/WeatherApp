package com.example.weatherapp.domain.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [WeatherHistoryEntity::class], version = 1)
abstract class WeatherHistoryDataBase : RoomDatabase() {
    abstract fun weatherHistoryDao(): WeatherHistoryDao
}