package com.example.weatherforecast.provider.location

import android.location.Location

class MyLocProvider: MyLocProviderInterface {
    var location: Location
        get() {
            return location
        }
        set(value) {
            location = value
        }

    override fun getDeviceLocation(): Location {
        return location
    }

//    companion object{
//        private var myLocProvider: MyLocProvider? = null
//
//        fun getInstance(): MyLocProvider{
//            if(myLocProvider == null){
//                myLocProvider = MyLocProvider()
//            }
//            return myLocProvider!!
//        }
//    }
}