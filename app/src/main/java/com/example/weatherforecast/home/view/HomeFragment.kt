package com.example.weatherforecast.home.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.FragmentHomeBinding
import com.example.weatherforecast.db.ConcreteLocalSource
import com.example.weatherforecast.home.viewmodel.HomeViewModel
import com.example.weatherforecast.home.viewmodel.HomeViewModelFactory
import com.example.weatherforecast.map.view.MapsActivity
import com.example.weatherforecast.model.Repository
import com.example.weatherforecast.model.WeatherResponse
import com.example.weatherforecast.network.WeatherClient
import com.example.weatherforecast.provider.Language.LanguageProvider
import com.example.weatherforecast.provider.unitsystem.UnitProvider
import com.example.weatherforecast.utils.GeoCoderConverter
import com.example.weatherforecast.utils.UnitSystem
import com.example.weatherforecast.utils.convertNumbersToArabic
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


class HomeFragment : Fragment() {
    private val binding by lazy { FragmentHomeBinding.inflate(layoutInflater) }
    private val factory by lazy { HomeViewModelFactory(Repository.getInstance(requireContext(), WeatherClient.getInstance(),
                ConcreteLocalSource(requireContext())), UnitProvider.getInstance(requireContext()), LanguageProvider.getInstance(requireContext()),
            requireActivity().application)
    }
    private val homeViewModel by lazy { ViewModelProvider(requireActivity(), factory)[HomeViewModel::class.java] }
    private val defaultPref by lazy{ PreferenceManager.getDefaultSharedPreferences(requireContext()) }

    private val mapPref by lazy{ requireContext().getSharedPreferences("mapPref",  Context.MODE_PRIVATE) }
    private var lastLocation: Location? = null
    private var fusedLocationClient: FusedLocationProviderClient? = null
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        setupRecyclerViews()
        setUnits()
        setDateTxt()

        val locationMode = defaultPref.getString("location", "map")

        checkPreferences(locationMode!!)

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
        homeViewModel.weather.observe(viewLifecycleOwner) {
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
        windValue.text = "${weatherResponse.current.wind_speed} m/s"
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
    private fun setAddressText(weatherResponse: WeatherResponse){
        val addressLine = GeoCoderConverter.getCityFromMarkedCoord(weatherResponse.lat, weatherResponse.lon, requireContext())
        if(!addressLine.equals("Connection Problem")){
            binding.addressTxt.text = addressLine
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setDateTxt(){
        val current = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy",Locale( defaultPref.getString("language", "en")))
        val formatted = current.format(formatter)
        binding.dateTxt.text = formatted
    }
    fun setUnits(){
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

    private fun checkPreferences(locationMode: String){
        if(locationMode.equals("gps")){
            getLastLocation()
        }else{

            var mapLat = mapPref.getString("lat", null)
            var mapLng = mapPref.getString("lng", null)
            if(! mapLat.equals(null) ){
                homeViewModel.getWeatherObject(mapLat!!.toDouble(), mapLng!!.toDouble())
            }
            else{
                Toast.makeText(requireContext(), "Please select a location from the map", Toast.LENGTH_LONG)
                val intent = Intent(requireContext(), MapsActivity::class.java)
                startActivity(intent)
            }

        }
    }


    private fun requestPermissions() {
        val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(
            requireActivity(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if (shouldProvideRationale) {
            showSnackbar("Location permission is needed for core functionality", "Okay") {
                startLocationPermissionRequest()
            }
        }
        else {
            //Log.i("TAG", "Requesting permission")
            startLocationPermissionRequest()
        }
    }
    private fun startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
            REQUEST_PERMISSIONS_REQUEST_CODE
        )
        getLastLocation()
    }
    private fun showSnackbar(
        mainTextStringId: String, actionStringId: String,
        listener: View.OnClickListener
    ) {
        Toast.makeText(requireContext(), mainTextStringId, Toast.LENGTH_LONG).show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            when {
                grantResults.isEmpty() -> {

                    Log.i("TAG", "User interaction was cancelled.")
                }
                grantResults[0] == PackageManager.PERMISSION_GRANTED -> {
                    Log.i("TAG", "User interaction was Accepted.")

                    getLastLocation()
                }
                else -> {
                    showSnackbar("Permission was denied", "Settings",
                        View.OnClickListener {
                            val intent = Intent()
                            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            val uri = Uri.fromParts(
                                "package",
                                Build.DISPLAY, null
                            )
                            intent.data = uri
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        }
                    )
                }
            }
        }
    }

    private fun getLastLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            requestPermissions()
            //getLastLocation()
        }
        else {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
            fusedLocationClient?.lastLocation!!.addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful && task.result != null) {
                    lastLocation = task.result
                    homeViewModel.getWeatherObject(lastLocation!!.latitude,lastLocation!!.longitude)
                }
                else {
                    Log.w("TAG", "getLastLocation:exception", task.exception)
                    homeViewModel.getWeatherFromLocaldb()
                }
            }
        }
    }



    companion object {
        const val REQUEST_PERMISSIONS_REQUEST_CODE: Int = 1
    }
}