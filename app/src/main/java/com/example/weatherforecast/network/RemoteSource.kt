package com.example.weatherforecast.network

interface RemoteSource {
    fun enqueueCall(networkDelegate: NetworkDelegate)

}