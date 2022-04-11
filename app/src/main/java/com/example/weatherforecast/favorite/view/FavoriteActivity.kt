package com.example.weatherforecast.favorite.view

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.ActivityFavoriteBinding
import com.example.weatherforecast.db.ConcreteLocalSource
import com.example.weatherforecast.favorite.viewmodel.FavoriteViewModel
import com.example.weatherforecast.favorite.viewmodel.FavoriteViewModelFactory
import com.example.weatherforecast.home.view.DaysAdapter
import com.example.weatherforecast.home.view.HoursAdapter
import com.example.weatherforecast.model.Repository
import com.example.weatherforecast.model.WeatherResponse
import com.example.weatherforecast.network.WeatherClient
import com.example.weatherforecast.provider.Language.LanguageProvider
import com.example.weatherforecast.provider.unitsystem.UnitProvider
import com.example.weatherforecast.utils.GeoCoderConverter
import com.example.weatherforecast.utils.UnitSystem
import com.example.weatherforecast.utils.convertNumbersToArabic
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class FavoriteActivity() : AppCompatActivity() {

    private val binding by lazy { ActivityFavoriteBinding.inflate(layoutInflater)}
    private val factory by lazy { FavoriteViewModelFactory(Repository.getInstance(this, WeatherClient.getInstance()
        ,ConcreteLocalSource(this)), UnitProvider.getInstance(this), LanguageProvider.getInstance(this))}
    private val favViewModel by lazy { ViewModelProvider(this, factory)[FavoriteViewModel::class.java] }
    private val defaultPref by lazy{ PreferenceManager.getDefaultSharedPreferences(this) }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupRecyclerViews()
        setupRecyclerViews()
        setUnits()
        setDateTxt()

        var latitude: Double = intent.extras!!.get("lat") as Double
        var longitude: Double = intent.extras!!.get("lng") as Double
        favViewModel.getWeatherObject(latitude, longitude)

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
            setWeatherStateImag(it)
            setAddressText(it)
            binding.progressBar.visibility = ProgressBar.INVISIBLE
        }
    }

    private fun fillWeatherData(weatherResponse: WeatherResponse) = binding.apply {
        if( defaultPref.getString("language", "en").equals("ar")){
            tempTxt.text = convertNumbersToArabic(weatherResponse.current.temp.toInt())
        }else{
            tempTxt.text = weatherResponse.current.temp.toInt().toString()
        }
        descTxt.text = weatherResponse.current.weather[0].description
        (hoursRecycler.adapter as HoursAdapter).setData(weatherResponse.hourly.slice(IntRange(0,23)))
        (daysRecycler.adapter as DaysAdapter).setData(weatherResponse.daily)
        pressureValue.text = "${weatherResponse.current.pressure} hpa"
        humadityValue.text = "${weatherResponse.current.humidity} %"
        if( defaultPref.getString("wind", "m/s").equals("m/s")){
            windValue.text = "${weatherResponse.current.wind_speed} m/s"
        }else{
            windValue.text = "${weatherResponse.current.wind_speed * 0.5} mph"
        }
        cloudValue.text = "${weatherResponse.current.clouds} %"
        uvValue.text = weatherResponse.current.uvi.toString()
        visibilityValue.text = "${weatherResponse.current.visibility} m"
    }
    private fun setWeatherStateImag(weatherResponse: WeatherResponse){
        when(weatherResponse.current.weather[0].main){
            "Clouds" -> binding.descImage.setImageResource(R.drawable.cloudy)
            "Clear" -> binding.descImage.setImageResource(R.drawable.sun)
            "Thunderstorm" -> binding.descImage.setImageResource(R.drawable.thunderstorm)
            "Drizzle" -> binding.descImage.setImageResource(R.drawable.drizzle)
            "Rain" -> binding.descImage.setImageResource(R.drawable.rain)
            "Snow" -> binding.descImage.setImageResource(R.drawable.snow)
            "Mist" -> binding.descImage.setImageResource(R.drawable.mist)
            "Smoke" -> binding.descImage.setImageResource(R.drawable.smoke)
            "Haze" -> binding.descImage.setImageResource(R.drawable.haze)
            "Dust" -> binding.descImage.setImageResource(R.drawable.dust)
            "Fog" -> binding.descImage.setImageResource(R.drawable.fog)
            "Sand" -> binding.descImage.setImageResource(R.drawable.dust)
            "Ash" -> binding.descImage.setImageResource(R.drawable.haze)
            "Squall" -> binding.descImage.setImageResource(R.drawable.squall)
            "Tornado" -> binding.descImage.setImageResource(R.drawable.thunderstorm)
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setDateTxt(){
        val current = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy",
            Locale( defaultPref.getString("language", "en"))
        )
        val formatted = current.format(formatter)
        binding.dateTxt.text = formatted
    }
    fun setUnits(){
        when(defaultPref.getString("language", "en")){
            "en" -> {
                val unit = defaultPref.getString("unit_system", "metric")
                if(unit!!.equals(UnitSystem.IMPERIAL.name)){
                    binding.unitTxt.text = " °F"
                }
                else if(unit!!.equals(UnitSystem.STANDARD.name)){
                    binding.unitTxt.text = " °K"
                }
                else{
                    binding.unitTxt.text = " °C"
                }
            }

            "ar" -> {
                val unit = defaultPref.getString("unit_system", "metric")
                if(unit!!.equals(UnitSystem.IMPERIAL.name)){
                    binding.unitTxt.text = " °ف"
                }
                else if(unit!!.equals(UnitSystem.STANDARD.name)){
                    binding.unitTxt.text = " °ك"
                }
                else{
                    binding.unitTxt.text = " °س"
                }
            }
        }

    }

    private fun setAddressText(weatherResponse: WeatherResponse){
        val addressLine = GeoCoderConverter.getCityFromMarkedCoord(weatherResponse.lat, weatherResponse.lon, this)
        if(!addressLine.equals("Connection Problem")){
            binding.addressTxt.text = addressLine
        }
    }
}