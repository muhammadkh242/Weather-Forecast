package com.example.weatherforecast.alerts.view

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.R
import com.example.weatherforecast.model.Alert
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class AlertsAdapter(private val context: Context, private val onAlertClickListener: OnAlertClickListener):
    RecyclerView.Adapter<AlertsAdapter.ViewHolder>() {

    private val defaultPref by lazy{ PreferenceManager.getDefaultSharedPreferences(context) }

    private var alertList: List<Alert> = listOf()



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.alert_row, parent, false)

        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentAlert = alertList[position]
        holder.alertDate.text = getDayFromTime(currentAlert.startPoint)
        holder.endDate.text = getDayFromTime(currentAlert.endPoint)

        holder.alertTime.text = getHourFromTime(currentAlert.start)
        holder.menuIcon.setOnClickListener {
            val popUp = PopupMenu(context, holder.menuIcon)
            popUp.inflate(R.menu.options_menu)
            popUp.setOnMenuItemClickListener {
                onAlertClickListener.onDeleteClick(alertList[position])
                return@setOnMenuItemClickListener false
            }

            popUp.show()

        }
    }

    override fun getItemCount(): Int {
        return alertList.size
    }

    fun setData(alerts: List<Alert>){
        alertList = alerts
        notifyDataSetChanged()
    }
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val alertDate: TextView = itemView.findViewById(R.id.alertDate)
        val alertTime: TextView = itemView.findViewById(R.id.alertTime)
        val menuIcon: TextView = itemView.findViewById(R.id.menuIcon)
        val endDate: TextView = itemView.findViewById(R.id.endDate)
    }


  fun getDayFromTime(time: Long): String{
      return SimpleDateFormat("dd MMMM",Locale(defaultPref.getString("language", "en"))).format(time*1000)
  }
    fun getHourFromTime(time: Long): String{
        return SimpleDateFormat("h:mm a", Locale(defaultPref.getString("language", "en"))).format(time*1000)
    }

}