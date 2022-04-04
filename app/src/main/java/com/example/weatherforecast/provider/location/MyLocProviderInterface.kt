package com.example.weatherforecast.provider.location

import android.location.Location

interface MyLocProviderInterface {
    fun getDeviceLocation(): Location

}