package com.example.weatherforecast.map.view

import android.content.Context
import android.content.Intent
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecast.MainActivity
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.ActivityMapsBinding
import com.example.weatherforecast.db.ConcreteLocalSource
import com.example.weatherforecast.map.viewmodel.MapViewModel
import com.example.weatherforecast.map.viewmodel.MapViewModelFactory
import com.example.weatherforecast.model.Favorite
import com.example.weatherforecast.model.Repository
import com.example.weatherforecast.network.WeatherClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private var selectedLat = ""
    private var selectedLng = ""
    private val mapPref by lazy{ this.getSharedPreferences("mapPref",  Context.MODE_PRIVATE) }
    private val factory by lazy{ MapViewModelFactory(Repository.getInstance(this,
    WeatherClient.getInstance(), ConcreteLocalSource(this))) }
    private val mapViewModel by lazy{ ViewModelProvider(this,factory)[MapViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        if(intent.extras?.get("map_request") != null){
            Log.i("TAG", "onCreate: ${intent.extras!!.get("map_request")}")

        }

        binding.doneBtn.setOnClickListener {
            if(intent.extras?.get("map_request") != null && intent.extras?.get("map_request")!!.equals("fav")){
                val addressLine = getCityFromMarkedCoord(selectedLat.toDouble(), selectedLng.toDouble())

                if(addressLine != "Connection Problem"){
                    Favorite(selectedLat.toDouble(), selectedLng.toDouble(),addressLine).apply {
                        saveMarkIntoFavPlaces(this)
                        Toast.makeText(this@MapsActivity, "Saved Place", Toast.LENGTH_SHORT).show()
                    }
                    finish()
                }
                else{
                    Toast.makeText(this@MapsActivity, "Save Failed, No Internet Connection", Toast.LENGTH_LONG).show()

                }

            }
            else{
                saveMarkIntoPref()
                var intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }


        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        Log.i("TAG", "onMapReady: ")
        mMap = googleMap

        val alex = LatLng(31.2001, 29.9187)
        var marker = mMap.addMarker(MarkerOptions().position(alex).draggable(true))
        selectedLat = marker!!.position.latitude.toString()
        selectedLng = marker!!.position.longitude.toString()
        mMap.moveCamera(CameraUpdateFactory.newLatLng(alex))

        mMap.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener{
            override fun onMarkerDrag(p0: Marker) {
            }

            override fun onMarkerDragEnd(p0: Marker) {
                selectedLat = p0.position.latitude.toString()
                selectedLng = p0.position.longitude.toString()
            }

            override fun onMarkerDragStart(p0: Marker) {
            }

        })

        mMap.setOnMapClickListener { p0 ->
            marker!!.position = p0
            selectedLat = marker.position.latitude.toString()
            selectedLng = marker.position.longitude.toString()
        }



    }

    private fun saveMarkIntoPref(){
        mapPref.edit().apply {
            putString("lat", selectedLat)
            putString("lng", selectedLng)
            apply()
            commit()
        }
    }

    private fun saveMarkIntoFavPlaces(favorite: Favorite){
        mapViewModel.addToFavorite(favorite)
    }

    private fun getCityFromMarkedCoord(lat: Double, lng: Double): String{
        try{
            val geoCoder = Geocoder(this, Locale.getDefault())
            val addresses = geoCoder.getFromLocation(lat,lng, 1)

            if(addresses[0].adminArea ==null && addresses[0].locality ==null){
                return addresses[0].countryName
            }
            else if (addresses[0].adminArea == null){
                return "${addresses[0].countryName}, ${addresses[0].locality}"
            }
            else if (addresses[0].locality == null){
                return "${addresses[0].countryName}, ${addresses[0].adminArea}"
            }
            return "${addresses[0].countryName}, ${addresses[0].adminArea}, ${addresses[0].locality}"

        }catch (e: IOException){
            return "Connection Problem"
        }
    }


}