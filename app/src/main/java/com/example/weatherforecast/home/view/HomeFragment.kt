package com.example.weatherforecast.home.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherforecast.databinding.FragmentHomeBinding
import com.example.weatherforecast.db.ConcreteLocalSource
import com.example.weatherforecast.home.viewmodel.HomeViewModel
import com.example.weatherforecast.home.viewmodel.HomeViewModelFactory
import com.example.weatherforecast.model.Repository
import com.example.weatherforecast.model.WeatherResponse
import com.example.weatherforecast.network.WeatherClient
import com.example.weatherforecast.provider.Language.LanguageProvider
import com.example.weatherforecast.provider.unitsystem.UnitProvider

class HomeFragment : Fragment() {
    private val binding by lazy { FragmentHomeBinding.inflate(layoutInflater) }
    private val factory by lazy {
        HomeViewModelFactory(
            requireContext(),
            Repository.getInstance(
                requireContext(), WeatherClient.getInstance(),
                ConcreteLocalSource(requireContext())
            ),
            UnitProvider.getInstance(requireContext()),
            LanguageProvider.getInstance(requireContext())
        )
    }
    private val homeViewModel by lazy { ViewModelProvider(requireActivity(), factory)[HomeViewModel::class.java] }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Getting Recycler View related items ready
        binding.daysRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.daysRecycler.adapter = DaysAdapter(requireContext())
        binding.hoursRecycler.layoutManager =  LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.hoursRecycler.adapter = HoursAdapter(requireContext())
        setData()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        homeViewModel.getWeatherObject()
        setData()
    }

    private fun setData() {
        homeViewModel.weather.observe(viewLifecycleOwner) {
            if (it != null) {
                fillWeatherData(it)
            } else {
                Log.i("TAG", "onCreateView: NULL LIVE DATA FROM ROOM")
            }
            binding.progressBar.visibility = ProgressBar.INVISIBLE
        }
    }

    private fun fillWeatherData(weatherResponse: WeatherResponse) {
        Log.i("TAG", "Response: HomeFragment ${weatherResponse.toString()}")
        Log.i("TAG", "Temp: ${weatherResponse.current.temp}")
        binding.timeZoneTxt.text = weatherResponse.timezone
        binding.tempTxt.text = weatherResponse.current.temp.toInt().toString()
        binding.descTxt.text = weatherResponse.current.weather[0].description
        (binding.hoursRecycler.adapter as HoursAdapter).setData(weatherResponse.hourly)
        (binding.daysRecycler.adapter as DaysAdapter).setData(weatherResponse.daily)
        binding.pressureValue.text = "${weatherResponse.current.pressure} hpa"
        binding.humadityValue.text = "${weatherResponse.current.humidity} %"
        binding.windValue.text = "${weatherResponse.current.wind_speed} m/s"
        binding.cloudValue.text = "${weatherResponse.current.clouds} %"
        binding.uvValue.text = weatherResponse.current.uvi.toString()
        binding.visibilityValue.text = "${weatherResponse.current.visibility} m"
    }
}