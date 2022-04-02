package com.example.weatherforecast.network

import android.util.Log
import com.example.weatherforecast.model.WeatherResponse
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherClient: RemoteSource {
    private var baseUrl: String = "https://api.openweathermap.org/data/2.5/"
    private lateinit var retrofit: Retrofit
    private var client: WeatherClient? = null

    override suspend fun getWeatherDefault(units: String, lat: String, lon: String): WeatherResponse {
        val weatherService = RetrofitHelper.getInstance().create(WeatherService::class.java)

        val response = weatherService.getDefaultWeather(units = units, lat = lat, lon = lon)

        return response
    }

    companion object{
        private var instance: WeatherClient? = null
        fun getInstance(): WeatherClient{
            return  instance?: WeatherClient()
        }
    }

}