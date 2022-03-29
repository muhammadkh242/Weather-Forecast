package com.example.weatherforecast.model

import com.example.weatherforecast.network.NetworkDelegate

interface RepositoryInterface {
    fun getWeather(networkDelegate: NetworkDelegate)
}