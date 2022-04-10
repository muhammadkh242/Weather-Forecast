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

class Worker(val context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    private val repo by lazy{ Repository.getInstance(context, WeatherClient.getInstance(),
        ConcreteLocalSource(context)) }

    private val CHANNEL_ID = "CHANNEL_ID"

    override suspend fun doWork(): Result {
        val weatherResponse = repo.getWeatherOffline()
        if(weatherResponse.alerts.isNotEmpty()){
            showNotification((weatherResponse.alerts)[0].event.toString())
        }
        return Result.success()

    }
    fun showNotification(event: String){
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
            .setContentTitle("Alert ")
            .setContentText(event)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true).build()
        val managerCompat = NotificationManagerCompat.from(context)
        managerCompat.notify(7, notification)
    }

}