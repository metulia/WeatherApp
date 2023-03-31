package com.example.weatherapp.domain.room

import android.database.Cursor
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

    @Query("SELECT * FROM weather_history_table")
    fun getWeatherHistoryCursor(): Cursor

    @Query("SELECT * FROM weather_history_table WHERE id = :id")
    fun getWeatherHistoryCursor(id: Long): Cursor

    @Query("DELETE FROM weather_history_table WHERE id = :id")
    fun deleteById(id: Long)
}