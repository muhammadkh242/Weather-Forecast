package com.example.weatherforecast.map.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.model.Favorite
import com.example.weatherforecast.model.RepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MapViewModel(private val _repo: RepositoryInterface) : ViewModel() {
    fun addToFavorite(favorite: Favorite){
        viewModelScope.launch(Dispatchers.IO){
            _repo.insertFavorite(favorite)

        }
    }
}