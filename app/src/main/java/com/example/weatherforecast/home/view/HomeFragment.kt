package com.example.weatherforecast.home.view

import android.os.Bundle
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
            Repository.getInstance(
                requireContext(), WeatherClient.getInstance(),
                ConcreteLocalSource(requireContext())
            ),
            UnitProvider.getInstance(requireContext()),
            LanguageProvider.getInstance(requireContext()),
            requireActivity().application
        )
    }
    private val homeViewModel by lazy { ViewModelProvider(requireActivity(), factory)[HomeViewModel::class.java] }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setupRecyclerViews()
        setData()
        return binding.root
    }

    private fun setupRecyclerViews() = binding.apply {
        daysRecycler.layoutManager = LinearLayoutManager(requireContext())
        daysRecycler.adapter = DaysAdapter(requireContext())
        hoursRecycler.layoutManager =  LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        hoursRecycler.adapter = HoursAdapter(requireContext())
    }

    override fun onResume() {
        super.onResume()
        homeViewModel.getWeatherObject()
    }

    private fun setData() {
        homeViewModel.weather.observe(viewLifecycleOwner) {
            fillWeatherData(it)
            binding.progressBar.visibility = ProgressBar.INVISIBLE
        }
    }

    private fun fillWeatherData(weatherResponse: WeatherResponse) = binding.apply {
        timeZoneTxt.text = weatherResponse.timezone
        tempTxt.text = weatherResponse.current.temp.toInt().toString()
        descTxt.text = weatherResponse.current.weather[0].description
        (hoursRecycler.adapter as HoursAdapter).setData(weatherResponse.hourly)
        (daysRecycler.adapter as DaysAdapter).setData(weatherResponse.daily)
        pressureValue.text = "${weatherResponse.current.pressure} hpa"
        humadityValue.text = "${weatherResponse.current.humidity} %"
        windValue.text = "${weatherResponse.current.wind_speed} m/s"
        cloudValue.text = "${weatherResponse.current.clouds} %"
        uvValue.text = weatherResponse.current.uvi.toString()
        visibilityValue.text = "${weatherResponse.current.visibility} m"
    }
}