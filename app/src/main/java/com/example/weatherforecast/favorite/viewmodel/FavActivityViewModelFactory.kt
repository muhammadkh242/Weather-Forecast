package com.example.weatherforecast.favorite.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecast.model.RepositoryInterface
import com.example.weatherforecast.provider.Language.LanguageProviderInterface
import com.example.weatherforecast.provider.unitsystem.UnitProviderInterface

class FavActivityViewModelFactory(
    private val _repo: RepositoryInterface,
    private val unitProvider: UnitProviderInterface,
    private val languageProvider: LanguageProviderInterface,
   // private var lat: String,
    //private var lng: String
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if(modelClass.isAssignableFrom(FavActivityViewModel::class.java)){
                FavActivityViewModel(_repo, unitProvider, languageProvider) as T
            }else{
                throw IllegalArgumentException("not found view model class")
            }
        }
}