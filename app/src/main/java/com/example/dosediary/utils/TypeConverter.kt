package com.example.dosediary.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.*

class TypeConverter {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault())
    private val gson: Gson = GsonBuilder().setDateFormat(dateFormat.toPattern()).create()

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromDateList(dates: List<Date>): String {
        return gson.toJson(dates)
    }

    @TypeConverter
    fun toDateList(datesString: String): List<Date> {
        val type = object : TypeToken<List<Date>>() {}.type
        return gson.fromJson(datesString, type)
    }

    @TypeConverter
    fun fromMap(value: Map<Date, Boolean>?): String {
        val mapString = value?.mapKeys { dateFormat.format(it.key) }
        return gson.toJson(mapString)
    }

    @TypeConverter
    fun toMap(value: String): Map<Date, Boolean>? {
        val type = object : TypeToken<Map<String, Boolean>>() {}.type
        val stringMap: Map<String, Boolean> = gson.fromJson(value, type)
        return stringMap.mapKeys { dateFormat.parse(it.key) }
    }
}
