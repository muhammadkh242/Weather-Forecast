package com.example.weatherforecast.home.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.R
import com.example.weatherforecast.home.viewmodel.HomeViewModel
import com.example.weatherforecast.home.viewmodel.HomeViewModelFactory
import com.example.weatherforecast.model.Repository
import com.example.weatherforecast.network.WeatherClient

class HomeFragment : Fragment(){

    lateinit var factory: HomeViewModelFactory
    lateinit var homeViewModel: HomeViewModel
    lateinit var tempTxt: TextView
    lateinit var descTxt: TextView
    lateinit var dailyRecycler: RecyclerView
    lateinit var dailyAdapter: DaysAdapter
    lateinit var layoutManager: LinearLayoutManager
    lateinit var hourlyRecycler: RecyclerView
    lateinit var hourlyAdapter: HoursAdapter
    lateinit var hLayoutManager: LinearLayoutManager
    lateinit var pressureValue: TextView
    lateinit var humadityValue: TextView
    lateinit var windValue: TextView
    lateinit var cloudValue: TextView
    lateinit var uvValue: TextView
    lateinit var visibilityValue: TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        //Getting ViewModel Ready
        factory = HomeViewModelFactory(Repository.getInstance(requireContext(), WeatherClient.getInstance()))
        homeViewModel = ViewModelProvider(this, factory).get(HomeViewModel::class.java)

        //Getting UI Ready
        initUI(view)

        //Getting Recycler View related items ready
        dailyAdapter = DaysAdapter(requireContext())
        layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = RecyclerView.VERTICAL
        dailyRecycler.layoutManager = layoutManager
        dailyRecycler.adapter = dailyAdapter

        hourlyAdapter = HoursAdapter(requireContext())
        hLayoutManager = LinearLayoutManager(requireContext())
        hLayoutManager.orientation = RecyclerView.HORIZONTAL
        hourlyRecycler.layoutManager = hLayoutManager
        hourlyRecycler.adapter = hourlyAdapter

        homeViewModel.weather.observe(this as LifecycleOwner,{
            Log.i("TAG", "Response: HomeFragment" + it.toString())
            Log.i("TAG", "Temp: " + it.current.temp)

            tempTxt.text = it.current.temp.toInt().toString()
            descTxt.text = it.current.weather[0].description

            dailyAdapter.setData(it.daily)

            hourlyAdapter.setData(it.hourly)

            pressureValue.text = it.current.pressure.toString() + " hpa"
            humadityValue.text = it.current.humidity.toString() + " %"
            windValue.text = it.current.wind_speed.toString() + " m/s"
            cloudValue.text = it.current.clouds.toString() + " %"
            uvValue.text = it.current.uvi.toString()
            visibilityValue.text = it.current.visibility.toString() + " m"
        })


        return view
    }

    fun initUI(view: View){
        tempTxt = view.findViewById(R.id.tempTxt)
        descTxt = view.findViewById(R.id.descTxt)

        dailyRecycler = view.findViewById(R.id.daysRecycler)
        hourlyRecycler = view.findViewById(R.id.hoursRecycler)

        pressureValue = view.findViewById(R.id.pressureValue)
        humadityValue = view.findViewById(R.id.humadityValue)
        windValue = view.findViewById(R.id.windValue)
        cloudValue = view.findViewById(R.id.cloudValue)
        uvValue = view.findViewById(R.id.uvValue)
        visibilityValue = view.findViewById(R.id.visibilityValue)

    }


}