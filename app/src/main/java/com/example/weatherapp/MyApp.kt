package com.example.weatherapp

import android.app.Application
import androidx.room.Room
import com.example.weatherapp.data.repository.room.MyDataBase
import com.example.weatherapp.data.repository.room.WeatherHistoryDao
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = this
    }

    companion object {
        private var db: MyDataBase? = null
        private var appContext: MyApp? = null
        private const val DB_NAME = "My.db"

        @OptIn(InternalCoroutinesApi::class)
        fun getWeatherHistoryDao(): WeatherHistoryDao {
            if (db == null) {
                synchronized(MyDataBase::class.java) {
                    if (appContext != null) {
                        db = Room.databaseBuilder(appContext!!, MyDataBase::class.java, DB_NAME)
                            .allowMainThreadQueries()
                            .build()
                    } else {
                        throw IllegalStateException("Application context is null")
                    }
                }
            }
            return db!!.weatherHistoryDao()
        }
    }
}