package com.example.weatherforecast.favorite.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.model.Favorite
import com.example.weatherforecast.model.RepositoryInterface
import com.example.weatherforecast.model.WeatherResponse
import com.example.weatherforecast.provider.Language.LanguageProviderInterface
import com.example.weatherforecast.provider.unitsystem.UnitProviderInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoriteViewModel(
    private val _repo: RepositoryInterface,
    private val unitProvider: UnitProviderInterface,
    private val languageProvider: LanguageProviderInterface
): ViewModel() {

    private var _weather: MutableLiveData<WeatherResponse> = MutableLiveData()
    var weather: LiveData<WeatherResponse> = _weather

    fun getFavorites(): LiveData<List<Favorite>> = _repo.favorites


    fun deleteFavorite(favorite: Favorite){
        viewModelScope.launch(Dispatchers.IO){
            _repo.deleteFavorite(favorite)
        }
    }


    fun getWeatherObject(lat: Double, lng: Double) {
        getCurrentWeather(unitProvider.getUnitSystem().name, lat.toString()!!, lng.toString()!!, languageProvider.getLanguage())

    }

    private fun getCurrentWeather(units: String, lat: String, lng: String, language: String) = viewModelScope.launch {
        Log.i("TAG", "getCurrentWeather For Favorite: $lat $lng")
        val weatherDefault = _repo.getCurrentWeather(units, lat, lng, language)
        withContext(Dispatchers.IO) { _weather.postValue(weatherDefault) }
    }


}


