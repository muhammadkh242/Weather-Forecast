package com.example.weatherforecast

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.weatherforecast.favorite.view.FavoriteActivity
import kotlinx.coroutines.*

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val coroutineScope = CoroutineScope(Dispatchers.Main + Job())
        coroutineScope.launch {
            delay(3000)
            startActivity(Intent(this@SplashActivity, InitActivity::class.java))
        }
    }
}