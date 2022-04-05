package com.example.weatherforecast.map

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.weatherforecast.MainActivity
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private var selectedLat = ""
    private var selectedLng = ""

    private val mapPref by lazy{ this.getSharedPreferences("mapPref",  Context.MODE_PRIVATE) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.doneBtn.setOnClickListener {
            saveMarkIntoPref()
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        Log.i("TAG", "onMapReady: ")
        mMap = googleMap

        val alex = LatLng(31.2001, 29.9187)
        var marker = mMap.addMarker(MarkerOptions().position(alex).draggable(true))

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

}