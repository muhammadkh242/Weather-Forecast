package com.example.weatherforecast

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.preference.PreferenceManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.example.weatherforecast.utils.Connection


class InitActivity : AppCompatActivity() {

    lateinit var preferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    private val defaultpref by lazy{ PreferenceManager.getDefaultSharedPreferences(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_init)
        initPrefs()
        val locationSetup = preferences.getString("location", null)
        if(locationSetup == null){
            showDialog()
        }
        else{
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
    private fun initPrefs(){
        preferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        editor = preferences.edit()
    }

    private fun showDialog(){
        val dialog = MaterialDialog(this).noAutoDismiss().customView(R.layout.initial_setup_dialog)
        dialog.findViewById<TextView>(R.id.apply_button).setOnClickListener {
            if(Connection.isOnline(this)){
                val locSetup = dialog.getCustomView().findViewById<RadioButton>(
                    dialog.getCustomView().findViewById<RadioGroup>(R.id.location_group).checkedRadioButtonId)

                defaultpref.edit().putString("location", locSetup.text.toString()).apply()

                editor.putString("location", locSetup.text.toString())
                editor.apply()
                dialog.dismiss()
                startActivity(Intent(this, MainActivity::class.java))
            }
            else{
                Toast.makeText(this, "Please check your internet connection and try again", Toast.LENGTH_LONG).show()
            }

        }

        dialog.show()
    }
}