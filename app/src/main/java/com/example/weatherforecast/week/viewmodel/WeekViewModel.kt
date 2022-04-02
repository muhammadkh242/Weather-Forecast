package com.example.weatherforecast.week.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.model.RepositoryInterface
import com.example.weatherforecast.model.WeatherResponse
import com.example.weatherforecast.provider.location.LocationProviderInterface
import com.example.weatherforecast.provider.unitsystem.UnitProviderInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WeekViewModel(
    private val _repo: RepositoryInterface,
    private val unitProvider: UnitProviderInterface,
    private val locationProvider: LocationProviderInterface


): ViewModel() {

    private var unit = unitProvider.getUnitSystem().name

    private val latitude: Double = viewModelScope.launch {
        locationProvider.getPreferredLocationLat()
    } as Double

    private val longitude: Double = viewModelScope.launch {
        locationProvider.getPreferredLocationLon()
    }as Double

    private var _weather: MutableLiveData<WeatherResponse> = MutableLiveData()
    var weather: LiveData<WeatherResponse> = _weather

    init {
        getWeatherDefault(unit, latitude, longitude)
    }

    fun getWeatherDefault(units: String, latitude: Double, longitude: Double){
        viewModelScope.launch {
            val weatherDefault = _repo.getWeatherDefault(units = units, lat = latitude.toString(), lon = longitude.toString())
            withContext(Dispatchers.Main){
                _weather.postValue(weatherDefault)
            }
        }
    }
}
