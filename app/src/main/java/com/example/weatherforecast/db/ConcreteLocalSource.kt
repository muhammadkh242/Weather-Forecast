package com.example.weatherforecast.db

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.weatherforecast.model.Alert
import com.example.weatherforecast.model.Favorite
import com.example.weatherforecast.model.WeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ConcreteLocalSource(context: Context): LocalSource {
    private val dao: WeatherDAO?
    override val favorites: LiveData<List<Favorite>>
    override val alerts: LiveData<List<Alert>>

    init {
        val db: AppDataBase = AppDataBase.getInstance(context)
        dao = db.weatherDAO()
        favorites = dao.favorites
        alerts = dao.alerts
    }

    override suspend fun getWeatherOffline(): WeatherResponse {
        return dao!!.getWeatherOffline()
    }

    override fun insertFavorite(favorite: Favorite) {
        dao!!.insertFvorite(favorite)
    }

    override suspend fun insertWeatherResponse(weatherResponse: WeatherResponse) {
        dao?.insertWeatherResponse(weatherResponse)
    }

    override fun deleteFavorite(favorite: Favorite) {
        dao?.deleteFavorite(favorite)
    }

    override fun insertAlert(alert: Alert) {
        dao?.insertAlert(alert)
    }

    override fun deleteAlert(alert: Alert) {
        dao?.deleteAlert(alert)
    }


}