package com.example.dosediary.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Date

class TypeConverter {

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
        val gson = Gson()
        val type = object : TypeToken<List<Long>>() {}.type
        val longDates = dates.map { it.time }
        return gson.toJson(longDates, type)
    }

    @TypeConverter
    fun toDateList(datesString: String): List<Date> {
        val gson = Gson()
        val type = object : TypeToken<List<Long>>() {}.type
        val longDates: List<Long> = gson.fromJson(datesString, type)
        return longDates.map { Date(it) }
    }

    @TypeConverter
    fun fromMap(value: Map<Date, Boolean>?): String {
        val gson = Gson()
        val type = object : TypeToken<Map<Long, Boolean>>() {}.type
        val longMap = value?.mapKeys { it.key.time }
        return gson.toJson(longMap, type)
    }

    @TypeConverter
    fun toMap(value: String): Map<Date, Boolean>? {
        val gson = Gson()
        val type = object : TypeToken<Map<Long, Boolean>>() {}.type
        val longMap: Map<Long, Boolean> = gson.fromJson(value, type)
        return longMap.mapKeys { Date(it.key) }
    }
}
