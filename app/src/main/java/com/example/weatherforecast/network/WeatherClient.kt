package com.example.weatherforecast.network

import android.util.Log
import com.example.weatherforecast.model.WeatherResponse
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object WeatherClient: RemoteSource {
    private var baseUrl: String = "https://api.openweathermap.org/data/2.5/"
    private lateinit var retrofit: Retrofit
    private var client: WeatherClient? = null

    fun getInstance(): WeatherClient{
        if(client == null){
            client = WeatherClient
        }
        return client!!
    }

    override fun enqueueCall(networkDelegate: NetworkDelegate) {
        Log.i("TAG", "enqueueCall: ")
        var gson: Gson = GsonBuilder().setLenient().create()
        retrofit = Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(
            GsonConverterFactory.create(gson)).build()

        var weatherService: WeatherService = retrofit.create(WeatherService::class.java)
        var call: Call<WeatherResponse> = weatherService.getWeather("31.25654", "32.28411",
            "c67c9ddb5f0fa54ea9629f71fd2412d2", "metric")
        call.enqueue(object: Callback<WeatherResponse> {
            override fun onResponse(
                call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                networkDelegate.onSuccessfulResult(response.body() as WeatherResponse)
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Log.i("TAG", "onFailure: "  + t.message)
            }
        })






//        call.enqueue(object: retrofit2.Callback<String>{
//            override fun onResponse(call: Call<String>, response: Response<String>) {
//                Log.i("TAG", "onResponse: Weather Client " + response.body().toString())
//                networkDelegate.onSuccessfulResult(response.body().toString())
//            }
//
//            override fun onFailure(call: Call<String>, t: Throwable) {
//                Log.i("TAG", "onFailure: " + t.message)
//            }
//
//        })
//
    }
}