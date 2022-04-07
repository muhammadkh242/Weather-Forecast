package com.example.weatherforecast.model

import androidx.lifecycle.LiveData
import retrofit2.Response

interface RepositoryInterface {
    //get weather over network
    suspend fun getCurrentWeather(units: String, lat: String, lon: String, lang: String): WeatherResponse

    //insert last fetched weather into local db
    suspend fun insertWeatherResponse(weatherResponse: WeatherResponse)
    //suspend fun insertoFromRemoteToLocal(units: String, lat: String, lon: String)

    //get stored weather response from local db
    suspend fun getWeatherOffline(): WeatherResponse

    //insert favorite into local db
    fun insertFavorite(favorite: Favorite)

    //get favorites
    val favorites: LiveData<List<Favorite>>

    //delete favorite
    fun deleteFavorite(favorite: Favorite)
    //get fav weather over network
    suspend fun getFavotiteWeather(units: String, lat: String, lon: String, lang: String): WeatherResponse
}