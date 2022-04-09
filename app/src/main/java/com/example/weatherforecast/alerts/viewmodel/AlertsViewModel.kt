package com.example.weatherforecast.alerts.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.model.Alert
import com.example.weatherforecast.model.RepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlertsViewModel(private val _repo: RepositoryInterface): ViewModel() {

    fun getAlerts(): LiveData<List<Alert>> = _repo.alerts

    fun addAlert(alert: Alert){
        viewModelScope.launch(Dispatchers.IO) { _repo.insertLert(alert) }
    }

    fun deleteAtert(alert: Alert){
        viewModelScope.launch(Dispatchers.IO){ _repo.deleteAlert(alert) }
    }
}