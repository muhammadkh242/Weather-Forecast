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
        val pref = PreferenceManager.getDefaultSharedPreferences(context)
        if(pref.getString("language","en").equals("ar")){
            holder.dayTempTxt.text = "${convertNumbersToArabic(currentDay.temp.min.toInt())} /  ${convertNumbersToArabic(currentDay.temp.max.toInt())}"
        }
        else{
            holder.dayTempTxt.text = currentDay.temp.min.toInt().toString() + " / " + currentDay.temp.max.toInt()

        }
        holder.dayDescTxt.text = currentDay.weather[0].description
        val iconUrl: String = "https://openweathermap.org/img/wn/${currentDay.weather[0].icon}@2x.png"
        Glide.with(context).load(iconUrl).centerCrop().into(holder.dayStateIcon)

    }

    override fun getItemCount(): Int {
        return days.size
    }

    fun setData(days: List<Daily>){
        Log.i("TAG", "setData: ${days.size}")

        this.days = days
        notifyDataSetChanged()

    }



    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val dayTxt: TextView = itemView.findViewById(R.id.dayTxt)
        val dayTempTxt: TextView = itemView.findViewById(R.id.dayTempTxt)
        val dayDescTxt: TextView = itemView.findViewById(R.id.dayDescTxt)
        val dayStateIcon: ImageView = itemView.findViewById(R.id.dayStateIcon)


    }

    fun getDayFromTime(time: Long): String{
        return SimpleDateFormat("EEE").format(time*1000)
    }
    fun convertNumbersToArabic(value: Int): String {
        return (value.toString() + "")
            .replace("1".toRegex(), "١").replace("2".toRegex(), "٢")
            .replace("3".toRegex(), "٣").replace("4".toRegex(), "٤")
            .replace("5".toRegex(), "٥").replace("6".toRegex(), "٦")
            .replace("7".toRegex(), "٧").replace("8".toRegex(), "٨")
            .replace("9".toRegex(), "٩").replace("0".toRegex(), "٠")
    }

}