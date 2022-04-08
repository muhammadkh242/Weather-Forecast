package com.example.weatherforecast.alerts.view

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.FragmentAlertsBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.util.*


class AlertsFragment : Fragment(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private val binding by lazy{ FragmentAlertsBinding.inflate(layoutInflater) }
    var day = 0
    var month = 0
    var year = 0
    var hour = 0
    var minute = 0

    var startDay = 0
    var startMonth = 0
    var startYear = 0
    var startHour = 0
    var startMinute = 0

    var endDay = 0
    var endMonth = 0
    var endYear = 0
    var endHour = 0
    var endMinute = 0

    var startDate: String = ""

    var end = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding.floatBtn.setOnClickListener {
            showDialog()
        }
        return binding.root
    }
    private fun getDateTimeCalender(){
        val cal: Calendar =  Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
        hour = cal.get(Calendar.HOUR)
        minute = cal.get(Calendar.MINUTE)
    }

    private fun pickStartDate(){
        getDateTimeCalender()
        DatePickerDialog(requireContext(),this, year, month, day).show()

    }
    private fun pickEndDate(){
        getDateTimeCalender()
        DatePickerDialog(requireContext(),this, year, month, day).show()
    }



    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
        if(end){
            endYear = p1
            endMonth = p2
            endDay = p3
        }else{
            startYear = p1
            startMonth = p2
            startDay = p3
        }


        getDateTimeCalender()

        TimePickerDialog(requireContext(),this,hour, minute, true).show()

    }

    override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
        if(end){
            endHour = p1
            endMinute = p2
            getEndDateInMillis()
        } else{
            startHour = p1
            startMinute = p2
            getStartDateInMillis()

        }

    }
    fun getStartDateInMillis(){
        val calendar: Calendar = Calendar.getInstance()
        calendar.set(startYear, startMonth, startDay, startHour, startMinute)
        val startPoint: Long = calendar.timeInMillis
        Log.i("TAG", "getStartDateInMillis: ${startPoint}")
    }
    fun getEndDateInMillis(){
        val calendar: Calendar = Calendar.getInstance()
        calendar.set(endYear, endMonth, endDay, endHour, endMinute)
        val endPoint: Long = calendar.timeInMillis
        Log.i("TAG", "getEndDateInMillis: ${endPoint}")
    }

    @SuppressLint("NewApi", "SimpleDateFormat")
    private fun showDialog(){
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.alert_dialog)
        dialog.findViewById<TextView>(R.id.fromDateTxt).text = LocalDate.now().toString()
        dialog.findViewById<TextView>(R.id.fromTimeTxt).text =
            SimpleDateFormat("h:mm a").format(LocalTime.now().second*1000)
        dialog.findViewById<TextView>(R.id.toDateTxt).text = LocalDate.now().toString()
        dialog.findViewById<TextView>(R.id.toTimeTxt).text =
            SimpleDateFormat("h:mm a").format(LocalTime.now().second*1000)

        dialog.findViewById<Button>(R.id.fromBtn).setOnClickListener {
            pickStartDate()
        }
        dialog.findViewById<Button>(R.id.toBtn).setOnClickListener {
            end = true
            pickStartDate()
            dialog.dismiss()
        }
        dialog.findViewById<Button>(R.id.saveBtn).setOnClickListener {
            //save into local db
        }
        dialog.show()
        val window: Window? = dialog.window
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

}