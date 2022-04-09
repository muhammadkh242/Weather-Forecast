package com.example.weatherforecast.db

import androidx.lifecycle.LiveData
import com.example.weatherforecast.model.Alert
import com.example.weatherforecast.model.Favorite
import com.example.weatherforecast.model.WeatherResponse

interface LocalSource {
    suspend fun insertWeatherResponse(weatherResponse: WeatherResponse)
    suspend fun getWeatherOffline(): WeatherResponse

    //insert favorite into local db
    fun insertFavorite(favorite: Favorite)

    //get favorites
    val favorites: LiveData<List<Favorite>>

    //delete favorite
    fun deleteFavorite(favorite: Favorite)

    //insert alert
    fun insertAlert(alert: Alert)

    //delete alert
    fun deleteAlert(alert: Alert)

    //get alerts
    val alerts: LiveData<List<Alert>>



}