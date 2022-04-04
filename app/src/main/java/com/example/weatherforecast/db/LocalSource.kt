package com.example.weatherforecast.db

import androidx.lifecycle.LiveData
import com.example.weatherforecast.model.WeatherResponse

interface LocalSource {
    suspend fun insertWeatherResponse(weatherResponse: WeatherResponse)
    suspend fun getWeatherOffline(): WeatherResponse
}