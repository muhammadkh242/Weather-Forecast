package com.example.weatherforecast.provider.location

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.example.weatherforecast.model.WeatherResponse
import com.example.weatherforecast.provider.PreferencesProvider
import com.example.weatherforecast.provider.unitsystem.UnitProvider
import com.example.weatherforecast.utils.asDeferred
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.Deferred

const val USE_DEVICE_LOCATION = "use_device_location"
const val CUSTOM_LOCATION = "custom_location"

class LocationProvider(
    context: Context
): PreferencesProvider(context), LocationProviderInterface {
    private val _context = context.applicationContext
    private val fusedLocationProviderClient: FusedLocationProviderClient = FusedLocationProviderClient(_context)



    companion object{

        private var locationProvider: LocationProvider? = null

        fun getInstance(context: Context): LocationProvider {
            if(locationProvider == null){
                locationProvider = LocationProvider(context)
            }
            return locationProvider!!
        }
    }

    //three overridden methods
    override suspend fun hasLocationChanged(lastWeatherResponse: WeatherResponse): Boolean {
        //hasDeviceLocationChanged throws exception so we have to catch it

        val deviceLocationChanged = try {
            hasDeviceLocationChanged(lastWeatherResponse)
        }
        catch (e: Exception){
            return false
        }
        return deviceLocationChanged || hasCustomLocationChanged(lastWeatherResponse)

    }

    override suspend fun getPreferredLocationLat(): Double {
        if(isUsingDeviceLocation()){
            try{
                val deviceLocation = getLastDeviceLocation().await()
                    ?: return getCustomLocationLat()
                return deviceLocation.latitude
            }
            catch (e: ExceptionInInitializerError){
                return getCustomLocationLat()
            }

        }
        else
            return getCustomLocationLat()
    }

    override suspend fun getPreferredLocationLon(): Double {
        if(isUsingDeviceLocation()){
            try{
                val deviceLocation = getLastDeviceLocation().await()
                    ?:return getCustomLocationLon()
                return deviceLocation.longitude
            }
            catch (e: ExceptionInInitializerError){
                return getCustomLocationLon()
            }

        }
        else
            return getCustomLocationLon()
    }


    private suspend fun hasDeviceLocationChanged(lastWeatherResponse: WeatherResponse): Boolean{
        if(!isUsingDeviceLocation()){
            return false
        }

        //if null return false
        //Awaits for completion of this value without blocking a thread
        val deviceLocation = getLastDeviceLocation().await()
            ?: return false
        val comparison = 0.03

        return Math.abs(deviceLocation.latitude - lastWeatherResponse.lat) > comparison &&
                Math.abs(deviceLocation.longitude - lastWeatherResponse.lon) > comparison
    }

    private fun isUsingDeviceLocation(): Boolean{
        return pref.getBoolean(USE_DEVICE_LOCATION, true)
    }

    //Deferred value is a non-blocking cancellable future
    //suppress this because we did check manually
    //task need to be converted to deferred
    @SuppressLint("MissingPermission")
    private fun getLastDeviceLocation(): Deferred<Location> {
        if(hasLocationPermission()){
            //lastLocation maybe return null so, we need to observe location manually in app start ->> MainActivity
            return fusedLocationProviderClient.lastLocation.asDeferred()
        }
        else{
            throw Exception("Location Permission Not Granted")
        }
    }

    private fun hasLocationPermission(): Boolean{
        return ContextCompat.checkSelfPermission(_context,
            android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED
    }

    private fun hasCustomLocationChanged(lastWeatherResponse: WeatherResponse): Boolean{
        val customLocationLat = getCustomLocationLat()
        val customLocationLon = getCustomLocationLon()
        return customLocationLat != lastWeatherResponse.lat || customLocationLon != lastWeatherResponse.lon
    }

    private fun getCustomLocationLat(): Double {
        //return preferred lat
        return 31.25654
    }
    private fun getCustomLocationLon(): Double{
        //return preferred lon
        return 32.28411
    }
}
