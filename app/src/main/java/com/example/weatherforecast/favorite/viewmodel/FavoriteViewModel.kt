package com.example.weatherforecast.favorite.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.model.Favorite
import com.example.weatherforecast.model.RepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoriteViewModel(
    private val _repo: RepositoryInterface): ViewModel() {

    fun getFavorites(): LiveData<List<Favorite>>{
        return _repo.favorites

    }
}

