package com.example.weatherforecast.home.view

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.R
import com.example.weatherforecast.model.Hourly
import com.bumptech.glide.Glide
import com.example.weatherforecast.utils.convertNumbersToArabic
import java.text.SimpleDateFormat


class HoursAdapter (private val context: Context): RecyclerView.Adapter<HoursAdapter.ViewHolder>() {

    private var hours: List<Hourly> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HoursAdapter.ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.hour_row, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: HoursAdapter.ViewHolder, position: Int) {
        val currentHour = hours[position]
        val pref = PreferenceManager.getDefaultSharedPreferences(context)
        if(pref.getString("language","en").equals("ar")){
            holder.hourTempTxt.text = convertNumbersToArabic(currentHour.temp.toInt())
        }
        else{
            holder.hourTempTxt.text = currentHour.temp.toInt().toString()
        }
        holder.hourTxt.text = getHourFromTime(currentHour.dt)
        when (currentHour.weather[0].main) {
            "Clouds" -> holder.hourStateIcon.setImageResource(R.drawable.cloudy)
            "Clear" -> holder.hourStateIcon.setImageResource(R.drawable.sun)
            "Thunderstorm" -> holder.hourStateIcon.setImageResource(R.drawable.thunderstorm)
            "Drizzle" -> holder.hourStateIcon.setImageResource(R.drawable.drizzle)
            "Rain" -> holder.hourStateIcon.setImageResource(R.drawable.rain)
            "Snow" -> holder.hourStateIcon.setImageResource(R.drawable.snow)
            "Mist" -> holder.hourStateIcon.setImageResource(R.drawable.mist)
            "Smoke" -> holder.hourStateIcon.setImageResource(R.drawable.smoke)
            "Haze" -> holder.hourStateIcon.setImageResource(R.drawable.haze)
            "Dust" -> holder.hourStateIcon.setImageResource(R.drawable.dust)
            "Fog" -> holder.hourStateIcon.setImageResource(R.drawable.fog)
            "Sand" -> holder.hourStateIcon.setImageResource(R.drawable.dust)
            "Ash" -> holder.hourStateIcon.setImageResource(R.drawable.haze)
            "Squall" -> holder.hourStateIcon.setImageResource(R.drawable.squall)
            "Tornado" -> holder.hourStateIcon.setImageResource(R.drawable.thunderstorm)
        }
//        val iconUrl = "https://openweathermap.org/img/wn/${currentHour.weather[0].icon}@2x.png"
//        Glide.with(context).load(iconUrl).centerCrop().into(holder.hourStateIcon)

    }

    override fun getItemCount(): Int {
        return hours.size
    }

    fun setData(hours: List<Hourly>){
        this.hours = hours
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val hourTxt: TextView = itemView.findViewById(R.id.hourTxt)
        val hourTempTxt: TextView = itemView.findViewById(R.id.hourTempTxt)
        val hourStateIcon: ImageView = itemView.findViewById(R.id.hourStateIcon)

    }

    fun getHourFromTime(time: Long): String{
        return SimpleDateFormat("h:mm a").format(time*1000)
    }

}