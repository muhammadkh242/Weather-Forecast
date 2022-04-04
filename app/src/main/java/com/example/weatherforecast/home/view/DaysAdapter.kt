package com.example.weatherforecast.home.view

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherforecast.R
import com.example.weatherforecast.model.Daily
import java.text.SimpleDateFormat

class DaysAdapter(private val context: Context): RecyclerView.Adapter<DaysAdapter.ViewHolder>() {

    private var days: List<Daily> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.day_row, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentDay = days[position]
        holder.dayTxt.text = getDayFromTime(currentDay.dt)
        holder.dayTempTxt.text = currentDay.temp.min.toInt().toString() + " / " + currentDay.temp.max.toInt()
        holder.dayDescTxt.text = currentDay.weather[0].description
        val iconUrl: String = "https://openweathermap.org/img/wn/${currentDay.weather[0].icon}@2x.png"
        Glide.with(context).load(iconUrl).centerCrop().into(holder.dayStateIcon)

    }

    override fun getItemCount(): Int {
        return days.size
    }

    fun setData(days: List<Daily>){
        this.days = days
        notifyDataSetChanged()
        Log.i("TAG", "Days List Size: " + days.size)

    }



    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val dayTxt: TextView = itemView.findViewById(R.id.dayTxt)
        val dayTempTxt: TextView = itemView.findViewById(R.id.dayTempTxt)
        val dayDescTxt: TextView = itemView.findViewById(R.id.dayDescTxt)
        val dayStateIcon: ImageView = itemView.findViewById(R.id.dayStateIcon)


    }

    fun getDayFromTime(time: Long): String{
        return SimpleDateFormat("EEEE").format(time*1000)
    }

}