package com.example.weatherforecast.week.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecast.home.viewmodel.HomeViewModel
import com.example.weatherforecast.model.RepositoryInterface
import com.example.weatherforecast.provider.location.LocationProviderInterface
import com.example.weatherforecast.provider.unitsystem.UnitProviderInterface

class WeekViewModelFactory(
    private val _repo: RepositoryInterface,
    private val unitProvider: UnitProviderInterface,
    private val locationProvider: LocationProviderInterface


) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(WeekViewModel::class.java)){
            WeekViewModel(_repo, unitProvider, locationProvider) as T
        }else{
            throw IllegalArgumentException("not found view model class")
        }    }
}