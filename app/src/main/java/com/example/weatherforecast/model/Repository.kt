package com.example.weatherforecast.model

import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.example.weatherforecast.alerts.worker.Worker
import com.example.weatherforecast.db.LocalSource
import com.example.weatherforecast.network.RemoteSource
import java.util.concurrent.TimeUnit

class Repository(var context: Context, var remoteSource: RemoteSource, var localSource: LocalSource, ) : RepositoryInterface {

    var requests: MutableList<WorkRequest> = mutableListOf()

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


    //send requests to worker
    override fun sendRequests(alerts: LiveData<List<Alert>>) {
        alerts.observe(context as LifecycleOwner){
            Log.i("TAG", "sendRequests: ${it.size}")
            for (i in 0..it.size-1){
                Log.i("TAG", "Alert Duration: ${it[i].duration}")
                val request = OneTimeWorkRequest.Builder(Worker::class.java)
                    .setInitialDelay(it[i].duration, TimeUnit.MINUTES)
                    .build()
                requests.add(request)

            }
            WorkManager.getInstance(context).enqueue(requests)
        }
    }




}