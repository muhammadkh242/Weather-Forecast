package com.example.weatherforecast.favorite.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.model.Favorite
import com.example.weatherforecast.model.RepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteViewModel(
    private val _repo: RepositoryInterface
    ): ViewModel() {



    fun getFavorites(): LiveData<List<Favorite>>{
        return _repo.favorites
    }

    fun deleteFavorite(favorite: Favorite){
        viewModelScope.launch(Dispatchers.IO){
            _repo.deleteFavorite(favorite)
        }
    }
}


