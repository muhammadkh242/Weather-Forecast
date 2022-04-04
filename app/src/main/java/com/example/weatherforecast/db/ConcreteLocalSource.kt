package com.example.weatherforecast.db

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.weatherforecast.model.WeatherResponse

class ConcreteLocalSource(context: Context, ): LocalSource {
    private val dao: WeatherDAO?

    override suspend fun getWeatherOffline(): WeatherResponse {
        return dao!!.getWeatherOffline()
    }

    init {
        val db: AppDataBase = AppDataBase.getInstance(context)
        dao = db.weatherDAO()
    }

    //insert last weather response fetched to local db
    override suspend fun insertWeatherResponse(weatherResponse: WeatherResponse) {
        dao?.insertWeatherResponse(weatherResponse)
    }
}