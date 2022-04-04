package com.example.weatherforecast.network

import com.example.weatherforecast.model.WeatherResponse
import retrofit2.Response
import retrofit2.Retrofit

class WeatherClient: RemoteSource {

    override suspend fun getCurrentWeather(units: String, lat: String, lon: String, lang: String): WeatherResponse {
        val weatherService = RetrofitHelper.getInstance().create(WeatherService::class.java)

        val response = weatherService.getCurrentWeather(units = units, lat = lat, lon = lon, lang = lang)

        return response
    }

    companion object{
        private var instance: WeatherClient? = null
        fun getInstance(): WeatherClient{
            return  instance?: WeatherClient()
        }
    }

}