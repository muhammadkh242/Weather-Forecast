package com.example.weatherforecast.network

import retrofit2.Response
import retrofit2.http.GET

interface WeatherService {
    @GET("data/2.5/onecall?lat={31.205753}&lon={29.924526}&exclude={current}&appid={c67c9ddb5f0fa54ea9629f71fd2412d2}")
    suspend fun getWeather(): Response<String>
}