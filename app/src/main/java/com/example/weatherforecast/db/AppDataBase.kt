package com.example.weatherforecast.db

import android.content.Context
import androidx.room.*
import com.example.weatherforecast.model.WeatherResponse

@Database(entities = [WeatherResponse::class], version =1)
@TypeConverters(DataConverter::class)

abstract class AppDataBase: RoomDatabase() {

    abstract fun weatherDAO(): WeatherDAO
    companion object {

        private var INSTANCE: AppDataBase? = null

        //one thread at a time to access this method
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