package com.example.weatherforecast.model

interface RepositoryInterface {
    suspend fun getWeatherDefault(units: String, lat: String, lon: String): WeatherResponse
}