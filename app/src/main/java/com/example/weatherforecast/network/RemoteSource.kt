package com.example.weatherforecast.network

import com.example.weatherforecast.model.WeatherResponse

interface RemoteSource {
    suspend fun getWeatherDefault(): WeatherResponse

}