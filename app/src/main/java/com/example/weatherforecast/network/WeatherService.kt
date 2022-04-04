package com.example.weatherforecast.network

import com.example.weatherforecast.model.WeatherResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("onecall")

    suspend fun getCurrentWeather(
        @Query("lat") lat: String = "31.25654",
        @Query("lon") lon: String = "32.28411",
        @Query("APPID") app_id: String = "c67c9ddb5f0fa54ea9629f71fd2412d2",
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "en"

    ): WeatherResponse


}