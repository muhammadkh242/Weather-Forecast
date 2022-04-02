package com.example.weatherforecast.provider.unitsystem

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.example.weatherforecast.provider.PreferencesProvider
import com.example.weatherforecast.utils.UnitSystem

class UnitProvider(context: Context): PreferencesProvider(context),UnitProviderInterface {


    companion object{

        private var unitProvider: UnitProvider? = null

        fun getInstance(context: Context): UnitProvider{
            if(unitProvider == null){
                unitProvider = UnitProvider(context)
            }
            return unitProvider!!
        }
    }

    override fun getUnitSystem(): UnitSystem {
        val selectedUnit = pref.getString("unit_system", UnitSystem.METRIC.name)
        return UnitSystem.valueOf(selectedUnit!!)
    }
}