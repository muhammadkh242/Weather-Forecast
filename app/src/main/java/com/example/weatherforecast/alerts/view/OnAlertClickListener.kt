package com.example.weatherforecast.alerts.view

import com.example.weatherforecast.model.Alert

interface OnAlertClickListener {
    fun onDeleteClick(alert: Alert)
}