package com.example.weatherforecast.provider.location

import android.location.Location
import com.example.weatherforecast.model.WeatherResponse

interface LocationProviderInterface {
    suspend fun hasLocationChanged(lastWeatherResponse: WeatherResponse): Boolean
    suspend fun getPreferredLocationLat(): Double
    suspend fun getPreferredLocationLon(): Double

}