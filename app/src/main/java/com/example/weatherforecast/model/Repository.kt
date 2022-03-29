package com.example.weatherforecast.model

import android.content.Context
import android.util.Log
import com.example.weatherforecast.network.NetworkDelegate
import com.example.weatherforecast.network.RemoteSource

class Repository(context: Context, remoteSource: RemoteSource): RepositoryInterface {

    var remoteSource = remoteSource

    companion object{
        private var repo: Repository? = null

        fun getInstance(context: Context, remoteSource: RemoteSource): Repository{
            if(repo == null){
                repo = Repository(context, remoteSource)
            }
            return repo!!
        }

    }

    override fun getWeather(networkDelegate: NetworkDelegate) {
        Log.i("TAG", "getWeather: Repository")
        remoteSource.enqueueCall(networkDelegate)
    }
}