package com.example.dosediary.model

import java.util.Date

data class Medication(
    var medicationName: String,
    var startDate: Date,
    var endDate: Date,
    var refillDays: Number,
    var dosage: Number,
    var frequency: String,
    var owner: String
)