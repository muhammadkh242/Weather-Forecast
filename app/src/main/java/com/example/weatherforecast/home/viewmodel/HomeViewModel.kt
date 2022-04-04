package com.example.weatherforecast.home.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.model.RepositoryInterface
import com.example.weatherforecast.model.WeatherResponse
import com.example.weatherforecast.provider.Language.LanguageProviderInterface
import com.example.weatherforecast.provider.unitsystem.UnitProviderInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HomeViewModel(
    private val _repo: RepositoryInterface,
    private val unitProvider: UnitProviderInterface,
    private val languageProvider: LanguageProviderInterface,
    private val application: Application
) : ViewModel() {

    private var latitude: String? = "31.25654"
    private var longitude: String? = "32.28411"
    private var _weather: MutableLiveData<WeatherResponse> = MutableLiveData()
    var weather: LiveData<WeatherResponse> = _weather

    fun getWeatherObject() {
        if (isOnline()) {
            getCurrentWeather(unitProvider.getUnitSystem().name, latitude!!, longitude!!, languageProvider.getLanguage())
        } else {
            getWeatherFromLocaldb()
        }
    }

    private fun getCurrentWeather(units: String, lat: String, lng: String, language: String) = viewModelScope.launch {
        val weatherDefault = _repo.getCurrentWeather(units, lat, lng, language)
        withContext(Dispatchers.IO) { _weather.postValue(weatherDefault) }
    }


    private fun getWeatherFromLocaldb() = viewModelScope.launch { withContext(Dispatchers.IO) {
        _weather.postValue(_repo.getWeatherOffline())
    } }


    private fun isOnline(): Boolean {
        val connectivityManager =
            application.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager ?: return false
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}