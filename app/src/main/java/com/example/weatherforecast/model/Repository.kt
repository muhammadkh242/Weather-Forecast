package com.example.weatherforecast.model

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.weatherforecast.db.LocalSource
import com.example.weatherforecast.network.RemoteSource

class Repository(var context: Context, var remoteSource: RemoteSource, var localSource: LocalSource, ) : RepositoryInterface {


    companion object {
        private var repo: Repository? = null

        fun getInstance(
            context: Context,
            remoteSource: RemoteSource,
            localSource: LocalSource
        ): Repository {
            if (repo == null) {
                repo = Repository(context, remoteSource, localSource)
            }
            return repo!!
        }

    }

    //get weather over network
    override suspend fun getCurrentWeather(units: String, lat: String, lon: String, lang: String): WeatherResponse {
        val remoteWeather =
            remoteSource.getCurrentWeather(units = units, lat = lat, lon = lon, lang = lang)
        insertWeatherResponse(remoteWeather)
        return remoteWeather
    }


    //insert last fetched weather into local db
    override suspend fun insertWeatherResponse(weatherResponse: WeatherResponse) {
        localSource.insertWeatherResponse(weatherResponse)
    }

    override suspend fun getWeatherOffline(): WeatherResponse {
        return localSource.getWeatherOffline()
    }

    override fun insertFavorite(favorite: Favorite) {
        localSource.insertFavorite(favorite)

    }

    override val favorites: LiveData<List<Favorite>>
        get() = localSource.favorites

    override fun deleteFavorite(favorite: Favorite) {
        localSource.deleteFavorite(favorite)
    }

    override suspend fun getFavotiteWeather(units: String, lat: String, lng: String, lang: String): WeatherResponse {
        Log.i("TAG", "getFavotiteWeather: ${remoteSource.getCurrentWeather(units, lat, lng, lang)}")
        return remoteSource.getCurrentWeather(units, lat, lng, lang)
    }

    override fun insertLert(alert: Alert) {
        localSource.insertAlert(alert)
    }

    override fun deleteAlert(alert: Alert) {
        localSource.deleteAlert(alert)
    }

    override val alerts: LiveData<List<Alert>>
        get() = localSource.alerts


}