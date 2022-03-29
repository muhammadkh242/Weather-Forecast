package com.example.weatherforecast.network

import com.example.weatherforecast.model.WeatherResponse

interface NetworkDelegate {
    fun onSuccessfulResult(weatherResponse: WeatherResponse)

}