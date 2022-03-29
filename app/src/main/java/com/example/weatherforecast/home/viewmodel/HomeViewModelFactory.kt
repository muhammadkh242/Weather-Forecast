package com.example.weatherforecast.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecast.model.RepositoryInterface

class HomeViewModelFactory(private val _repo: RepositoryInterface): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(HomeViewModel::class.java)){
            HomeViewModel(_repo) as T
        }else{
            throw IllegalArgumentException("not found view model class")
        }

    }
}