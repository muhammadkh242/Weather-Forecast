package com.example.weatherforecast.network

import com.example.weatherforecast.model.WeatherResponse

interface RemoteSource {
    suspend fun getWeatherDefault(units: String, lat: String, lon: String): WeatherResponse

}