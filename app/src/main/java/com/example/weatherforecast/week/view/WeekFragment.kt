package com.example.weatherforecast.week.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.R
import com.example.weatherforecast.model.Repository
import com.example.weatherforecast.network.WeatherClient
import com.example.weatherforecast.provider.location.LocationProvider
import com.example.weatherforecast.provider.unitsystem.UnitProvider
import com.example.weatherforecast.week.viewmodel.WeekViewModel
import com.example.weatherforecast.week.viewmodel.WeekViewModelFactory


class WeekFragment : Fragment() {
    lateinit var factory: WeekViewModelFactory
    lateinit var weekViewModel: WeekViewModel
    lateinit var dailyRecycler: RecyclerView
    lateinit var dailyAdapter: DaysAdapter
    lateinit var layoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_week, container, false)

        factory = WeekViewModelFactory(Repository.getInstance(requireContext(),
            WeatherClient.getInstance()), UnitProvider.getInstance(requireContext()), LocationProvider.getInstance(requireContext()))

        weekViewModel = ViewModelProvider(this, factory).get(WeekViewModel::class.java)
        dailyRecycler = view.findViewById(R.id.daysRecycler)
        dailyAdapter = DaysAdapter(requireContext())
        layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = RecyclerView.VERTICAL
        dailyRecycler.layoutManager = layoutManager
        dailyRecycler.adapter = dailyAdapter

        weekViewModel.weather.observe(this as LifecycleOwner, {
            dailyAdapter.setData(it.daily)
        })

        return view
    }

}