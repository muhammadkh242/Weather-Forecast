package com.example.weatherforecast.alerts.worker

import android.content.Context
import android.util.Log
import androidx.work.*
import com.example.weatherforecast.db.ConcreteLocalSource
import com.example.weatherforecast.model.Repository
import com.example.weatherforecast.network.WeatherClient

class Worker(val context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    private val repo by lazy{ Repository.getInstance(context, WeatherClient.getInstance(),
        ConcreteLocalSource(context)) }

    override suspend fun doWork(): Result {
        val weatherResponse = repo.getWeatherOffline()
        for (i in 1..10000){
            Log.i("TAG", "doWork: ${weatherResponse.lat}")

        }
        //loop over alerts
        return Result.success()

    }

}