package com.example.weatherforecast.alerts.view

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.R
import com.example.weatherforecast.model.Alert

class AlertsAdapter(private val context: Context, private val onAlertClickListener: OnAlertClickListener):
    RecyclerView.Adapter<AlertsAdapter.ViewHolder>() {

    private var alertList: List<Alert> = listOf()



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.alert_row, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentAlert = alertList[position]
        holder.alertDate.text = currentAlert.startDate
        holder.alertTime.text = currentAlert.startTime
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
    }

}