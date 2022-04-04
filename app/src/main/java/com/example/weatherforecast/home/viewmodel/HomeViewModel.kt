package com.example.weatherforecast.home.viewmodel

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
    private val context: Context,
    private val _repo: RepositoryInterface,
    private val unitProvider: UnitProviderInterface,
    private val languageProvider: LanguageProviderInterface
) : ViewModel() {

    private var latitude: String? = "31.25654"
    private var longitude: String? = "32.28411"
    private var _weather: MutableLiveData<WeatherResponse> = MutableLiveData()
    var weather: LiveData<WeatherResponse> = _weather

    fun getWeatherObject() {
        if (isOnline(context)) {
            getCurrentWeather(unitProvider.getUnitSystem().name, latitude!!, longitude!!, languageProvider.getLanguage())
        } else {
            getWeatherFromLocaldb()
        }
    }

    fun getCurrentWeather(units: String, latitude: String, longitude: String, language: String) {
        viewModelScope.launch {
            val weatherDefault = _repo.getCurrentWeather(
                units = units,
                lat = latitude,
                lon = longitude,
                lang = language
            )
            withContext(Dispatchers.IO) {
                _weather.postValue(weatherDefault)
            }
        }
    }


    fun getWeatherFromLocaldb() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _weather.postValue(_repo.getWeatherOffline())
            }
        }
    }


    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager

        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }


}