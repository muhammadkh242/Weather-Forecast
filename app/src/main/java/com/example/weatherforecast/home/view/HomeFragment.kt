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
import com.example.weatherforecast.R
import com.example.weatherforecast.home.viewmodel.HomeViewModel
import com.example.weatherforecast.home.viewmodel.HomeViewModelFactory
import com.example.weatherforecast.model.Repository
import com.example.weatherforecast.network.NetworkDelegate
import com.example.weatherforecast.network.WeatherClient

class HomeFragment : Fragment(){

    lateinit var factory: HomeViewModelFactory
    lateinit var homeViewModel: HomeViewModel
    lateinit var tempTxt: TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        initUI(view)
        factory = HomeViewModelFactory(Repository.getInstance(requireContext(), WeatherClient.getInstance()))

        homeViewModel = ViewModelProvider(this, factory).get(HomeViewModel::class.java)

        homeViewModel.weather.observe(this as LifecycleOwner,{
            Log.i("TAG", "Response: HomeFragment" + it.toString())
            Log.i("TAG", "onCreateView: " + it.current.temp)
            tempTxt.text = it.current.temp.toString()
        })


//        WeatherClient.getInstance().enqueueCall()


        return view
    }

    fun initUI(view: View){
        tempTxt = view.findViewById(R.id.tempTxt)

    }


}