package com.example.weatherforecast.favorite.view

import com.example.weatherforecast.model.Favorite

interface OnItemClickListener {
    fun onClick(favorite: Favorite)
}