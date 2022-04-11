package com.example.weatherforecast.alerts.worker

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.*
import com.example.weatherforecast.R
import com.example.weatherforecast.db.ConcreteLocalSource
import com.example.weatherforecast.model.Repository
import com.example.weatherforecast.network.WeatherClient
import com.example.weatherforecast.provider.Language.LanguageProvider
import com.example.weatherforecast.provider.unitsystem.UnitProvider
import com.example.weatherforecast.utils.Connection

class Worker(val context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    private val repo by lazy{ Repository.getInstance(context, WeatherClient.getInstance(),
        ConcreteLocalSource(context)) }

    private val CHANNEL_ID = "CHANNEL_ID"

    override suspend fun doWork(): Result {
        Log.i("TAG", "doWork: ")
        val weatherResponse = repo.getWeatherOffline()
        Log.i("TAG", "doWork: ${weatherResponse.lat}")
        if(weatherResponse.alerts.isNullOrEmpty()){
            showNotification("No alerts for this time")
        }else{
            showNotification(weatherResponse.alerts!![0].event.toString())
        }
        /*        val weatherResponse = repo.getWeatherOffline()
        if(weatherResponse.alerts.isNotEmpty()){
            showNotification((weatherResponse.alerts)[0].event.toString())
        }*/
        /*if(Connection.isOnline(context)){
            val currentResponse = repo.getCurrentWeather(UnitProvider.getInstance(context).getUnitSystem().name, weatherResponse.lat.toString(),
            weatherResponse.lon.toString(), LanguageProvider.getInstance(context).getLanguage())
            if(currentResponse.alerts.isNotEmpty()){
                Log.i("TAG", "doWork: ${currentResponse.alerts.get(0).event.toString()}")
                showNotification(currentResponse.alerts.get(0).event.toString())
            }
            else{
                showNotification("No alerts for this time")
            }
        }
        else{
            if(weatherResponse.alerts.isNotEmpty()){
            showNotification((weatherResponse.alerts)[0].event.toString())
            }
            else{
                showNotification("No alerts for this time")
            }
        }*/
        //showNotification("alert")
        return Result.success()

    }
    fun showNotification(event: String){
        Log.i("TAG", "showNotification: ")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "MyChannel"
            val desc: String = "Description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel: NotificationChannel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = desc
            val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE)as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        val notification: Notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.bill)
            .setContentTitle("Weather Alert")
            .setContentText(event)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true).build()
        val managerCompat = NotificationManagerCompat.from(context)
        managerCompat.notify(7, notification)
    }

}