package com.example.weatherforecast.utils

import android.annotation.SuppressLint
import android.arch.lifecycle.OnLifecycleEvent
import android.os.Looper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest

class LifeCycleBoundLocationManager(
    lifeCycleOwner: LifecycleOwner,
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    private val locationCallback: LocationCallback
): LifecycleObserver {

    init {
        lifeCycleOwner.lifecycle.addObserver(this)
    }

    //apply -> i can apply operations on the object and return it
    //instead of creating new instance and after that do some setters stuff
    private val locationRequest = LocationRequest().apply {
        this.interval = 5000
        fastestInterval = 5000
        priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
    }

    @OnLifecycleEvent(android.arch.lifecycle.Lifecycle.Event.ON_RESUME)
    @SuppressLint("MissingPermission")
    fun startLocationUpdates(){
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }


    @OnLifecycleEvent(android.arch.lifecycle.Lifecycle.Event.ON_PAUSE)
    fun removeLocationUpdates(){
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }
}