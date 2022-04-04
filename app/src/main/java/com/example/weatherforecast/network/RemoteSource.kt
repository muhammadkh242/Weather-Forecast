package com.example.weatherforecast.network

import com.example.weatherforecast.model.WeatherResponse
import retrofit2.Response

interface RemoteSource {
    suspend fun getCurrentWeather(units: String, lat: String, lon: String, lang: String): WeatherResponse

}