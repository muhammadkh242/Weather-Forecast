package com.example.weatherforecast.alerts.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecast.favorite.viewmodel.FavoriteViewModel
import com.example.weatherforecast.model.RepositoryInterface

class AlertsViewModelFactory(private val _repo: RepositoryInterface): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(AlertsViewModel::class.java)){
            AlertsViewModel(_repo) as T
        }else{
            throw IllegalArgumentException("not found view model class")
        }
    }
}