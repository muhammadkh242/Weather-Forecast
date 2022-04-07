package com.example.weatherforecast.network

import android.util.Log
import com.example.weatherforecast.model.WeatherResponse
import retrofit2.Response
import retrofit2.Retrofit

class WeatherClient: RemoteSource {

    override suspend fun getCurrentWeather(units: String, lat: String, lng: String, lang: String): WeatherResponse {
        val weatherService = RetrofitHelper.getInstance().create(WeatherService::class.java)
        return weatherService.getCurrentWeather(units, lat, lng, lang)
    }

    companion object{
        private var instance: WeatherClient? = null
        fun getInstance(): WeatherClient{
            return  instance?: WeatherClient()
        }
    }
}