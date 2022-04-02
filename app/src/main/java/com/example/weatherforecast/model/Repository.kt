package com.example.weatherforecast.model

import android.content.Context
import com.example.weatherforecast.network.RemoteSource

class Repository(context: Context, remoteSource: RemoteSource): RepositoryInterface {

    var remoteSource = remoteSource

    companion object{
        private var repo: Repository? = null

        fun getInstance(context: Context, remoteSource: RemoteSource): Repository{
            if(repo == null){
                repo = Repository(context, remoteSource)
            }
            return repo!!
        }

    }

    override suspend fun getWeatherDefault(units: String, lat: String, lon: String): WeatherResponse {
        return remoteSource.getWeatherDefault(units = units, lat = lat, lon = lon)
    }


}