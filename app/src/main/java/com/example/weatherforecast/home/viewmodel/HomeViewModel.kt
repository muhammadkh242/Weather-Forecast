package com.example.weatherforecast.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.model.RepositoryInterface
import com.example.weatherforecast.model.WeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(private val _repo: RepositoryInterface): ViewModel() {

    private var _weather: MutableLiveData<WeatherResponse> = MutableLiveData()
    var weather: LiveData<WeatherResponse> = _weather

    init {
        getWeatherDefault()
    }

    fun getWeatherDefault(){
        viewModelScope.launch {
            val weatherDefault = _repo.getWeatherDefault()
            withContext(Dispatchers.Main){
                _weather.postValue(weatherDefault)
            }
        }
    }



}