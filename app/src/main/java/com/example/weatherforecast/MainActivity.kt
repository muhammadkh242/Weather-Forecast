package com.example.weatherforecast

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.weatherforecast.utils.LifeCycleBoundLocationManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    private final val MY_PERMISSION_REQUEST_CODE: Int = 1
    lateinit var navController: NavController
    lateinit var bottomNav: BottomNavigationView
    lateinit var appBarConfiguration: AppBarConfiguration

    lateinit var  fusedLocationProviderClient: FusedLocationProviderClient

    private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(p0: LocationResult?) {
            super.onLocationResult(p0)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        fusedLocationProviderClient = FusedLocationProviderClient(applicationContext)



        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        bottomNav = findViewById(R.id.bottom_nav)
        bottomNav.setupWithNavController(navController)
        appBarConfiguration = AppBarConfiguration.Builder(R.id.homeFragment, R.id.weekFragment, R.id.settingsFragment)
            .build()

        NavigationUI.setupActionBarWithNavController(this,navController, appBarConfiguration)

        requestLocationPermission()

        if(hasLocationPermission()){
            bindLocationManager()
        }
        else{
            requestLocationPermission()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController,null)
    }

    private fun requestLocationPermission(){
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION), MY_PERMISSION_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == MY_PERMISSION_REQUEST_CODE){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                bindLocationManager()
            }
            else{
                Toast.makeText(this, "Set Location Manually From Settingd", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun hasLocationPermission(): Boolean{
        return ContextCompat.checkSelfPermission(this,
            android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED
    }

    private fun bindLocationManager(){
        LifeCycleBoundLocationManager(this,
        fusedLocationProviderClient,locationCallback)
    }




}