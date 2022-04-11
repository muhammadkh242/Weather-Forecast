package com.example.weatherforecast.db

import android.content.Context
import androidx.room.*
import com.example.weatherforecast.model.Alert
import com.example.weatherforecast.model.Favorite
import com.example.weatherforecast.model.WeatherResponse

@Database(entities = [WeatherResponse::class, Favorite::class, Alert::class], version =9)
@TypeConverters(DataConverter::class)

abstract class AppDataBase: RoomDatabase() {

    abstract fun weatherDAO(): WeatherDAO
    companion object {
        @Volatile
        private var INSTANCE: AppDataBase? = null
        @Synchronized
        fun getInstance(context: Context): AppDataBase{
            return INSTANCE?: Room.databaseBuilder(
                context.applicationContext,
                AppDataBase::class.java,
                "weather_response"
            ).fallbackToDestructiveMigration().build()
        }
    }

}