package com.example.weatherforecast.week.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecast.model.RepositoryInterface

class FavoriteViewModelFactory(
    private val _repo: RepositoryInterface


) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(FavoriteViewModel::class.java)){
            FavoriteViewModel(_repo) as T
        }else{
            throw IllegalArgumentException("not found view model class")
        }    }
}