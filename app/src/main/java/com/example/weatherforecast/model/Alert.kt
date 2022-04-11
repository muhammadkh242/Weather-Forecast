package com.example.weatherforecast.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alerts",  primaryKeys = ["startPoint", "start"])

data class Alert(
    @ColumnInfo(name = "startPoint")
    var startPoint: Long,
    @ColumnInfo(name = "endPoint")

    var endPoint: Long,
    @ColumnInfo(name = "start")

    var start: Long,

    @ColumnInfo(name = "duration")
    var duration:Long

)