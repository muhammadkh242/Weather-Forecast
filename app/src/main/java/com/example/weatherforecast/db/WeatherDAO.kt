package com.example.weatherforecast.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.weatherforecast.model.Alert
import com.example.weatherforecast.model.Favorite
import com.example.weatherforecast.model.WeatherResponse


@Dao
interface WeatherDAO {

    //insert weather response
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeatherResponse(weatherResponse: WeatherResponse)

    //get stored weather response
    @Query("SELECT * From weather_response")
    fun getWeatherOffline(): WeatherResponse

    //insert to favorite
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertFvorite(favorite: Favorite)

    //get favorite
    @get:Query("SELECT * From favorite")
    val favorites: LiveData<List<Favorite>>

    //delete favorite item
    @Delete
    fun deleteFavorite(favorite: Favorite)

    //insert alert
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAlert(alert: Alert)

    //delete alert
    @Delete
    fun deleteAlert(alert: Alert)

    //get alerts
    @get:Query("SELECT * fROM alerts")
    val alerts: LiveData<List<Alert>>




}