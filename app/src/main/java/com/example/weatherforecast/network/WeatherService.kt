package com.example.weatherforecast.network

import com.example.weatherforecast.model.WeatherResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("onecall")
    suspend fun getCurrentWeather(
        @Query("units") units: String = "metric",
        @Query("lat") lat: String = "61.5240",
        @Query("lon") lon: String = "105.3188",
        @Query("lang") lang: String = "en",
        @Query("APPID") app_id: String = "c67c9ddb5f0fa54ea9629f71fd2412d2"
    ): WeatherResponse
}
//31.18918918918919
//30.076999418531887