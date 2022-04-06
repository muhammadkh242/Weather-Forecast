package com.example.weatherforecast.db

import android.content.Context
import androidx.room.*
import com.example.weatherforecast.model.Favorite
import com.example.weatherforecast.model.WeatherResponse

@Database(entities = [WeatherResponse::class, Favorite::class], version =3)
@TypeConverters(DataConverter::class)

abstract class AppDataBase: RoomDatabase() {

    abstract fun weatherDAO(): WeatherDAO
    companion object {
        @Volatile
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