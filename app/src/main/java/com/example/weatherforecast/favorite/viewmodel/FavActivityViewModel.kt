package com.example.weatherforecast.favorite.viewmodel

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

class FavActivityViewModel(
    private val _repo: RepositoryInterface,
    private val unitProvider: UnitProviderInterface,
    private val languageProvider: LanguageProviderInterface,
): ViewModel() {



    private var _weather: MutableLiveData<WeatherResponse> = MutableLiveData()
    var weather: LiveData<WeatherResponse> = _weather

    suspend fun getFavoriteWeather(units: String, lat: String, lng: String, language: String) = viewModelScope.launch {
        val weatherDefault = _repo.getFavotiteWeather(units, lat, lng, language)
        withContext(Dispatchers.IO) { _weather.postValue(weatherDefault) }
    }
    suspend fun setLocation(lat: String, lng: String){
        getFavoriteWeather(unitProvider.getUnitSystem().name, lat, lng,languageProvider.getLanguage())
    }




}