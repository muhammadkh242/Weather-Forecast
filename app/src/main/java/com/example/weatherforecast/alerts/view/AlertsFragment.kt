package com.example.weatherforecast.alerts.view

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.example.weatherforecast.R
import com.example.weatherforecast.alerts.viewmodel.AlertsViewModel
import com.example.weatherforecast.alerts.viewmodel.AlertsViewModelFactory
import com.example.weatherforecast.alerts.worker.Worker
import com.example.weatherforecast.databinding.FragmentAlertsBinding
import com.example.weatherforecast.db.ConcreteLocalSource
import com.example.weatherforecast.model.Alert
import com.example.weatherforecast.model.Repository
import com.example.weatherforecast.network.WeatherClient
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.util.*
import java.util.concurrent.TimeUnit


class AlertsFragment : Fragment(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, OnAlertClickListener {

    private val binding by lazy{ FragmentAlertsBinding.inflate(layoutInflater) }
    private val factory by lazy { AlertsViewModelFactory(Repository.getInstance(requireContext(), WeatherClient.getInstance(),
    ConcreteLocalSource(requireContext()))) }
    private val alertsViewModel by lazy { ViewModelProvider(requireActivity(), factory) [AlertsViewModel::class.java]}

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

    var currentPoint: Long = 0L
    var startPoint: Long = 0L
    var endPoint: Long = 0L
    var duration: Long = 0L
    var end = false

    var startTime: String=""
    var startDate: String = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding.noAlertsImage.visibility = View.INVISIBLE
        binding.noAlertsTxt.visibility = View.INVISIBLE
        setUpRecyclerView()

        observeAlerts()

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

    private fun pickDate(){
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
            startDate = "$startYear-$startMonth-$startDay"
        }


        getDateTimeCalender()

        TimePickerDialog(requireContext(),this,hour, minute, true).show()

    }

    override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
        if(end){
            endHour = p1
            endMinute = p2
            getEndDateInMillis()
            end = false
        } else{
            startHour = p1
            startMinute = p2
            getStartDateInMillis()
            startTime = "$startHour:$startMinute"

        }

    }
    fun getStartDateInMillis(){
        val calendar: Calendar = Calendar.getInstance()
        calendar.set(startYear, startMonth, startDay, startHour, startMinute)
        startPoint = calendar.timeInMillis
        getDateTimeCalender()
        Log.i("TAG", "Current Time $ Date: $year $month $day $hour $minute")
        calendar.set(year,month,day,hour,minute)
        currentPoint = calendar.timeInMillis
        duration = ((startPoint - currentPoint)/60000)
        //startRequest()
    }
    fun getEndDateInMillis(){
        val calendar: Calendar = Calendar.getInstance()
        calendar.set(endYear, endMonth, endDay, endHour, endMinute)
        endPoint = calendar.timeInMillis
        //duration = ((endPoint - startPoint)/60000)
    }
    fun startRequest(){
        Log.i("TAG", "startRequest: ")
        var request = OneTimeWorkRequest.Builder(Worker::class.java)
            .setInitialDelay(duration, TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(requireContext()).enqueue(request)
        duration = 0L
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
            pickDate()
        }
        dialog.findViewById<Button>(R.id.toBtn).setOnClickListener {
            end = true
            pickDate()
        }
        dialog.findViewById<Button>(R.id.saveBtn).setOnClickListener {
            binding.noAlertsImage.visibility = View.INVISIBLE
            binding.noAlertsTxt.visibility = View.INVISIBLE
            dialog.dismiss()
            var alert = Alert(startDate, startTime, duration)
            alertsViewModel.addAlert(alert)
            Log.i("TAG", "Duration: $duration")
            Toast.makeText(requireContext(), "Saved Alert", Toast.LENGTH_SHORT).show()
            startRequest()


        }
        dialog.show()
        val window: Window? = dialog.window
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    private fun setUpRecyclerView() = binding.apply {
        alertsRecycler.layoutManager = LinearLayoutManager(requireContext())
        alertsRecycler.adapter =  AlertsAdapter(requireContext(),this@AlertsFragment)
    }

    private fun observeAlerts(){
        alertsViewModel.getAlerts().observe(requireActivity()){
            fillAlertsData(it)
            checkAlertsListSize(it)

        }
    }
    private fun fillAlertsData(alerts: List<Alert>) = binding.apply {
        (alertsRecycler.adapter as AlertsAdapter).setData(alerts)
    }
    private fun checkAlertsListSize(alerts: List<Alert>){
        if(alerts.isEmpty()){
            binding.noAlertsImage.visibility = View.VISIBLE
            binding.noAlertsTxt.visibility = View.VISIBLE
        }
    }

    override fun onDeleteClick(alert: Alert) {
        alertsViewModel.deleteAtert(alert)
        Toast.makeText(context, "Deleted Alert", Toast.LENGTH_SHORT).show()
    }


}