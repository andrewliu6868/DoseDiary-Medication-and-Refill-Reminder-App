package com.example.dosediary.utils

import androidx.room.TypeConverter
import com.google.android.gms.maps.model.LatLng
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TypeConverter {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault())

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
        return dates.joinToString(",") { dateFormat.format(it) }
    }

    @TypeConverter
    fun toDateList(datesString: String): List<Date> {
        return datesString.split(",").map { dateFormat.parse(it) }
    }

    @TypeConverter
    fun fromLatLng(latLng: String): LatLng {
        val (latitude, longitude) = latLng.split(",")
        return LatLng(latitude.toDouble(), longitude.toDouble())
    }

    @TypeConverter
    fun toLatLng(latLng: LatLng): String {
        return "${latLng.latitude},${latLng.longitude}"
    }
}