package com.example.weatherforecast.provider.Language

import android.content.Context
import com.example.weatherforecast.provider.PreferencesProvider

class LanguageProvider(context: Context): PreferencesProvider(context), LanguageProviderInterface {


    companion object{
        private var languageProvider: LanguageProvider? = null

        fun getInstance(context: Context): LanguageProvider{
            if(languageProvider == null){
                languageProvider = LanguageProvider(context)
            }
            return languageProvider!!
        }
    }
    override fun getLanguage(): String {
        val selectedLang = pref.getString("language", "en")
        return selectedLang!!
    }
}