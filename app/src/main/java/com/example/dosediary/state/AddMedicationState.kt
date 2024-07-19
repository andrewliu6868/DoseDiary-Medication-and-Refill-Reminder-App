package com.example.dosediary.state

import android.app.TimePickerDialog
import java.util.Date

data class AddMedicationState(
    val medicationName: String = "",
    val startDate: Date = Date(),
    val endDate: Date = Date(),
    val frequency: String = "1",
    val times: List<Date> = listOf(Date()),
    val refillDays: Int = 50,
    val note: String = "",
    val address: String = "",
    val postalCode: String = "",
    val postalCodeError: String? = null,
    val showConfirmDialog: Boolean = false
)