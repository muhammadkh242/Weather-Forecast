package com.example.weatherforecast.provider

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

abstract class PreferencesProvider(context: Context) {
    private val _context = context.applicationContext

    protected val pref: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(_context)


}