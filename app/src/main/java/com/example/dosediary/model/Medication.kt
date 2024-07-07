package com.example.dosediary.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class Medication(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var medicationName: String,
    var startDate: Date,
    var endDate: Date,
    var refillDays: Number,
    var dosage: Number,
    var frequency: String,
    var owner: String
)