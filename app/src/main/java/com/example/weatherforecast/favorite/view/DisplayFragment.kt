package com.example.weatherforecast.favorite.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.FragmentDisplayBinding
import com.example.weatherforecast.favorite.viewmodel.FavoriteViewModel
import com.example.weatherforecast.home.view.DaysAdapter
import com.example.weatherforecast.home.view.HoursAdapter
import com.example.weatherforecast.model.WeatherResponse
import com.example.weatherforecast.utils.convertNumbersToArabic


class DisplayFragment(val viewModel: FavoriteViewModel) : Fragment() {
    private val binding by lazy { FragmentDisplayBinding.inflate(layoutInflater) }
    private val defaultPref by lazy{ PreferenceManager.getDefaultSharedPreferences(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setupRecyclerViews()
        observeWeather()
        return binding.root
    }
    private fun setupRecyclerViews() = binding.apply {
        daysRecycler.layoutManager = LinearLayoutManager(requireContext())
        daysRecycler.adapter = DaysAdapter(requireContext())
        hoursRecycler.layoutManager =  LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        hoursRecycler.adapter = HoursAdapter(requireContext())
    }
    private fun observeWeather() {
        viewModel.weather.observe(viewLifecycleOwner) {
            fillWeatherData(it)
            //setWeatherStateImag(it)
            //setAddressText(it)
            binding.progressBar.visibility = ProgressBar.INVISIBLE
        }
    }
    private fun fillWeatherData(weatherResponse: WeatherResponse) = binding.apply {
        if(defaultPref.getString("language", "en").equals("ar")){
            tempTxt.text = convertNumbersToArabic(weatherResponse.current.temp.toInt())
        }else{
            tempTxt.text = weatherResponse.current.temp.toInt().toString()
        }
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