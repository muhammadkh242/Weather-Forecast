package com.example.weatherforecast.favorite.view

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.ActivityFavoriteBinding
import com.example.weatherforecast.db.ConcreteLocalSource
import com.example.weatherforecast.favorite.viewmodel.FavActivityViewModel
import com.example.weatherforecast.favorite.viewmodel.FavActivityViewModelFactory
import com.example.weatherforecast.favorite.viewmodel.FavoriteViewModel
import com.example.weatherforecast.favorite.viewmodel.FavoriteViewModelFactory
import com.example.weatherforecast.home.view.DaysAdapter
import com.example.weatherforecast.home.view.HoursAdapter
import com.example.weatherforecast.model.Favorite
import com.example.weatherforecast.model.Repository
import com.example.weatherforecast.model.WeatherResponse
import com.example.weatherforecast.network.WeatherClient
import com.example.weatherforecast.provider.Language.LanguageProvider
import com.example.weatherforecast.provider.unitsystem.UnitProvider

class FavoriteActivity : AppCompatActivity() {

    private val binding by lazy { ActivityFavoriteBinding.inflate(layoutInflater)}
    private val factory by lazy { FavoriteViewModelFactory(Repository.getInstance(this, WeatherClient.getInstance()
        ,ConcreteLocalSource(this)), UnitProvider.getInstance(this), LanguageProvider.getInstance(this))}
    private val favViewModel by lazy { ViewModelProvider(this, factory)[FavoriteViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        setupRecyclerViews()

        val favorite: Favorite = intent.extras!!.get("favorite_obj")as Favorite

        favViewModel.getWeatherObject(favorite.lat, favorite.lng)

        observeWeather()

    }


    private fun setupRecyclerViews() = binding.apply {
        daysRecycler.layoutManager = LinearLayoutManager(this@FavoriteActivity)
        daysRecycler.adapter = DaysAdapter(this@FavoriteActivity)
        hoursRecycler.layoutManager =  LinearLayoutManager(this@FavoriteActivity, LinearLayoutManager.HORIZONTAL, false)
        hoursRecycler.adapter = HoursAdapter(this@FavoriteActivity)
    }


    private fun observeWeather(){
        favViewModel.weather.observe(this) {
            fillWeatherData(it)
        }
    }

    private fun fillWeatherData(weatherResponse: WeatherResponse) = binding.apply {
        Log.i("TAG", "fillWeatherData Favorite Activity: ${weatherResponse.timezone}")
        test.text = weatherResponse.timezone
        timeZoneTxt.text = weatherResponse.timezone
        tempTxt.text = weatherResponse.current.temp.toInt().toString()
        descTxt.text = weatherResponse.current.weather[0].description
        (hoursRecycler.adapter as HoursAdapter).setData(weatherResponse.hourly.slice(IntRange(0,23)))
        (daysRecycler.adapter as DaysAdapter).setData(weatherResponse.daily)
        pressureValue.text = "${weatherResponse.current.pressure} hpa"
        humadityValue.text = "${weatherResponse.current.humidity} %"
        windValue.text = "${weatherResponse.current.wind_speed} m/s"
        cloudValue.text = "${weatherResponse.current.clouds} %"
        uvValue.text = weatherResponse.current.uvi.toString()
        visibilityValue.text = "${weatherResponse.current.visibility} m"
    }
}