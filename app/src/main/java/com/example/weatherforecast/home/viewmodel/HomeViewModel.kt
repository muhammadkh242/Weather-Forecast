package com.example.weatherforecast.home.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherforecast.model.RepositoryInterface
import com.example.weatherforecast.model.WeatherResponse
import com.example.weatherforecast.network.NetworkDelegate

class HomeViewModel(private val _repo: RepositoryInterface): ViewModel(), NetworkDelegate {
    init {
        _repo.getWeather(this)
    }

    private var _weather: MutableLiveData<WeatherResponse> = MutableLiveData()
    var weather: LiveData<WeatherResponse> = _weather

    override fun onSuccessfulResult(weatherResponse: WeatherResponse) {
        Log.i("TAG", "onSuccessfulResult: HomeViewModel ")
        _weather.postValue(weatherResponse)

    }

}