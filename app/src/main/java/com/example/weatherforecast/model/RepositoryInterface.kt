package com.example.weatherforecast.model

interface RepositoryInterface {
    suspend fun getWeatherDefault(): WeatherResponse
}