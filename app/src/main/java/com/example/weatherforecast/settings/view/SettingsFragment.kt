package com.example.weatherforecast.settings.view


import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.example.weatherforecast.R
import java.util.*


class SettingsFragment : PreferenceFragmentCompat(){
    private val defaultPref by lazy{ PreferenceManager.getDefaultSharedPreferences(requireContext()) }

    override fun onCreatePreferences(p0: Bundle?, p1: String?) {
        addPreferencesFromResource(R.xml.preferences)
    }

    override fun onPause() {
        super.onPause()
        val lang = defaultPref.getString("language", "en")
        val config = this.resources.configuration
        val locale = Locale(lang)
        Locale.setDefault(locale)
        config.setLocale(locale)

        requireActivity().createConfigurationContext(config)
        this.resources.updateConfiguration(config, this.resources.displayMetrics)
    }


}