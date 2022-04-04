package com.example.weatherforecast.home.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecast.model.RepositoryInterface
import com.example.weatherforecast.provider.Language.LanguageProviderInterface
import com.example.weatherforecast.provider.location.LocationProviderInterface
import com.example.weatherforecast.provider.unitsystem.UnitProviderInterface

class HomeViewModelFactory(
    private val context: Context,
    private val _repo: RepositoryInterface,
    private val unitProvider: UnitProviderInterface,
    private val languageProvider: LanguageProviderInterface
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(HomeViewModel::class.java)){
            HomeViewModel(context,_repo, unitProvider, languageProvider) as T
        } else {
            throw IllegalArgumentException("not found view model class")
        }

    }
}