package com.example.weatherforecast.utils

import android.content.Context
import android.location.Geocoder
import java.io.IOException
import java.util.*

class GeoCoderConverter {
    companion object{
         fun getCityFromMarkedCoord(lat: Double, lng: Double, context: Context): String{
            try{
                val geoCoder = Geocoder(context, Locale.getDefault())
                val addresses = geoCoder.getFromLocation(lat,lng, 1)

                if(addresses[0].adminArea ==null && addresses[0].locality ==null){
                    return addresses[0].countryName
                }
                else if (addresses[0].adminArea == null){
                    return "${addresses[0].countryName}, ${addresses[0].locality}"
                }
                else if (addresses[0].locality == null){
                    return "${addresses[0].countryName}, ${addresses[0].adminArea}"
                }
                return "${addresses[0].countryName}, ${addresses[0].adminArea}, ${addresses[0].locality}"

            }catch (e: IOException){
                return "Connection Problem"
            }
        }
    }
}