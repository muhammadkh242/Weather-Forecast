package com.example.weatherforecast.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.weatherforecast.model.Favorite
import com.example.weatherforecast.model.WeatherResponse


@Dao
interface WeatherDAO {

    //insert weather response
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeatherResponse(weatherResponse: WeatherResponse)

    //get stored weather response
   // @get:Query("SELECT * From weather_response where id = 0")
//    val weatherResponse: LiveData<WeatherResponse>

    @Query("SELECT * From weather_response")
    fun getWeatherOffline(): WeatherResponse

    //insert to favorite
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertFvorite(favorite: Favorite)

    //get favorite
    @get:Query("SELECT * From favorite")
    val favorites: LiveData<List<Favorite>>
//    fun getFavorites(): List<Favorite>




}