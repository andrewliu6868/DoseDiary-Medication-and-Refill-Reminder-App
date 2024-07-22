package com.example.dosediary.model.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["owner"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Medication(
    val medicationName: String,
    val startDate: Date,
    val endDate: Date,
    val frequency: String,
    var times: List<Date>,
    val refillDays: Int,
    val note: String,
    val address: String,
    val postalCode: String,
    val postalCodeError: String?,
    var takenTimes: Map<Date, Boolean> = emptyMap(),
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val lastRefilledDate: Date,
    var owner: Int
)
