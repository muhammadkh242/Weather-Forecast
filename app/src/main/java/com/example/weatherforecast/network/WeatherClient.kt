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

class WeatherClient: RemoteSource {
    private var baseUrl: String = "https://api.openweathermap.org/data/2.5/"
    private lateinit var retrofit: Retrofit
    private var client: WeatherClient? = null

    override suspend fun getWeatherDefault(): WeatherResponse {
        val weatherService = RetrofitHelper.getInstance().create(WeatherService::class.java)

        val response = weatherService.getDefaultWeather()

        return response
    }

    companion object{
        private var instance: WeatherClient? = null
        fun getInstance(): WeatherClient{
            return  instance?: WeatherClient()
        }
    }


//    override fun enqueueCall(networkDelegate: NetworkDelegate) {
//        Log.i("TAG", "enqueueCall: ")
//        var gson: Gson = GsonBuilder().setLenient().create()
//        retrofit = Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(
//            GsonConverterFactory.create(gson)
//        ).build()
//
//        var weatherService: WeatherService = retrofit.create(WeatherService::class.java)
//        var call: Call<WeatherResponse> = weatherService.getWeather(
//            "31.25654", "32.28411",
//            "c67c9ddb5f0fa54ea9629f71fd2412d2", "metric"
//        )
//        call.enqueue(object : Callback<WeatherResponse> {
//            override fun onResponse(
//                call: Call<WeatherResponse>, response: Response<WeatherResponse>
//            ) {
//                networkDelegate.onSuccessfulResult(response.body() as WeatherResponse)
//            }
//
//            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
//                Log.i("TAG", "onFailure: " + t.message)
//            }
//        })
//    }
//

}