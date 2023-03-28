package com.example.weatherapp.domain.room

import androidx.room.*

@Dao
interface WeatherHistoryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(entity: WeatherHistoryEntity)

    @Update
    fun update(entity: WeatherHistoryEntity)

    @Delete
    fun delete(entity: WeatherHistoryEntity)

    @Query("SELECT * FROM weather_history_table")
    fun getAll(): List<WeatherHistoryEntity>

    @Query("SELECT * FROM weather_history_table WHERE city LIKE :city")
    fun getCityWeatherHistory(city: String): List<WeatherHistoryEntity>
}