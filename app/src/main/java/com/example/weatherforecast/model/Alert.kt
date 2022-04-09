package com.example.weatherforecast.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alerts")

data class Alert(
    @ColumnInfo(name = "startDate")
    var startDate: String,
    @ColumnInfo(name = "startTime")
    var startTime: String,
    @ColumnInfo(name = "duration")
    @PrimaryKey
    var duration:Long
    )