package com.example.weatherforecast.db

import androidx.room.TypeConverter
import com.example.weatherforecast.model.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class DataConverter {
    @TypeConverter
    fun minutelyListToString(minutelyList: List<Minutely>?): String? {
        if (minutelyList == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Minutely>>(){}.type
        return gson.toJson(minutelyList, type)
    }

    @TypeConverter
    fun minutelyStringToList(minutelyString: String?): List<Minutely>? {
        if (minutelyString == null) {
            return emptyList()
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Minutely>>() {}.type
        return gson.fromJson(minutelyString, type)
    }


    @TypeConverter
    fun hourlyListToString(hourlyList: List<Hourly>?): String? {
        if (hourlyList == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Hourly>>(){}.type
        return gson.toJson(hourlyList, type)
    }

    @TypeConverter
    fun hourlyStringToList(hourlyString: String?): List<Hourly>? {
        if (hourlyString == null) {
            return emptyList()
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Hourly>>() {}.type
        return gson.fromJson(hourlyString, type)
    }

    @TypeConverter
    fun dailyListToString(dailyList: List<Daily>?): String? {
        if (dailyList == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Daily>>(){}.type
        return gson.toJson(dailyList, type)
    }

    @TypeConverter
    fun dailyStringToList(dailyString: String?): List<Daily>? {
        if (dailyString == null) {
            return emptyList()
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Daily>>() {}.type
        return gson.fromJson(dailyString, type)
    }

    @TypeConverter
    fun weatherListToString(weatherList: ArrayList<Weather>?): String? {
        if (weatherList == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<ArrayList<Weather>>(){}.type
        return gson.toJson(weatherList, type)
    }

    @TypeConverter
    fun weatherStringToList(weatherString: String?): ArrayList<Weather>? {
        if (weatherString == null) {
            return java.util.ArrayList()
        }
        val gson = Gson()
        val type: Type = object : TypeToken<ArrayList<Weather>>() {}.type
        return gson.fromJson(weatherString, type)
    }

   /* @TypeConverter
    fun currentToJson(current: Current?) = Gson().toJson(current)

    @TypeConverter
    fun jsonToCurrent(currentString: String) =
        Gson().fromJson(currentString, Current::class.java)

    @TypeConverter
    fun hourlyListToJson(hourlyList: List<Hourly>?) = Gson().toJson(hourlyList)

    @TypeConverter
    fun jsonToHourlyList(hourlyString: String) =
        Gson().fromJson(hourlyString, Array<Hourly>::class.java)?.toList()
    @TypeConverter
    fun minutelyListToJson(hourlyList: List<Minutely>?) = Gson().toJson(hourlyList)

    @TypeConverter
    fun jsonToMinutelyList(hourlyString: String) =
        Gson().fromJson(hourlyString, Array<Minutely>::class.java)?.toList()

    @TypeConverter
    fun dailyListToJson(dailyList: List<Daily>) = Gson().toJson(dailyList)

    @TypeConverter
    fun jsonToDailyList(dailyString: String) =
        Gson().fromJson(dailyString, Array<Daily>::class.java).toList()

    @TypeConverter
    fun weatherListToJson(weatherList: List<Weather>) = Gson().toJson(weatherList)

    @TypeConverter
    fun jsonToWeatherList(weatherString: String) =
        Gson().fromJson(weatherString, Array<Weather>::class.java).toList()
*/

}