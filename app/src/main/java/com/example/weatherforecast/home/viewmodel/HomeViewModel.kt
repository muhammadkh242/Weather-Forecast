package com.example.weatherforecast.home.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.model.RepositoryInterface
import com.example.weatherforecast.model.WeatherResponse
import com.example.weatherforecast.provider.Language.LanguageProviderInterface
import com.example.weatherforecast.provider.unitsystem.UnitProviderInterface
import com.example.weatherforecast.utils.Connection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HomeViewModel(
    private val _repo: RepositoryInterface,
    private val unitProvider: UnitProviderInterface,
    private val languageProvider: LanguageProviderInterface,
    private val application: Application
) : ViewModel() {

    private var _weather: MutableLiveData<WeatherResponse> = MutableLiveData()
    var weather: LiveData<WeatherResponse> = _weather
    private val _locationLiveData = MutableLiveData<LocationObject>()
    var locationLiveData: LiveData<LocationObject> = _locationLiveData

    fun getWeatherObject(lat: Double, lng: Double) {
        if (Connection.isOnline(application.applicationContext)) {
            getCurrentWeather(unitProvider.getUnitSystem().name, lat.toString()!!, lng.toString()!!, languageProvider.getLanguage())
        } else {
            Toast.makeText(application.applicationContext, "No internet connection", Toast.LENGTH_LONG)
            getWeatherFromLocaldb()
        }
    }

    private fun getCurrentWeather(units: String, lat: String, lng: String, language: String) = viewModelScope.launch {
        val weatherDefault = _repo.getCurrentWeather(units, lat, lng, language)
        withContext(Dispatchers.IO) { _weather.postValue(weatherDefault) }
    }


    fun getWeatherFromLocaldb() = viewModelScope.launch { withContext(Dispatchers.IO) {
        _weather.postValue(_repo.getWeatherOffline())
    } }

    fun setLastLocation(lat: Double, lng: Double) {
        Log.i("TAG", "setLastLocation: $lat $lng")
        _locationLiveData.value = LocationObject(lat, lng)

    }
}
data class LocationObject(val lat: Double, val lng: Double)