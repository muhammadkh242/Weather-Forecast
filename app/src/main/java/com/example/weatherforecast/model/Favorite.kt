package com.example.weatherforecast.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "favorite")
data class Favorite(
    @ColumnInfo(name = "latitude")
    @PrimaryKey
    var lat: Double,
    @ColumnInfo(name = "longitude")
    var lng: Double,
    @ColumnInfo(name = "address")
    var addressLine: String
): Serializable