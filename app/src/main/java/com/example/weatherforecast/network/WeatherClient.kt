package com.example.weatherforecast.network

import retrofit2.Retrofit

object WeatherClient {
    private var baseUrl: String = "https://openweathermap.org/"
    private lateinit var retrofit: Retrofit
    private var client: WeatherClient? = null

    fun getInstance(): WeatherClient{
        if(client == null){
            client = WeatherClient
        }
        return client!!
    }
}