package com.example.dosediary.state

import com.example.dosediary.model.entity.Medication
import java.util.Date

data class MedicationWithNextRefillDate(
    val medication: Medication,
    val nextRefillDate: Date?
)

data class MedRefillState(
    val medRefillsToday: List<MedicationWithNextRefillDate> = emptyList(),
    val medRefillsUpcoming: List<MedicationWithNextRefillDate> = emptyList(),
    val selectedRefillDetail: Medication = defaultMedication()
)

fun defaultMedication(): Medication{
    return Medication(
        medicationName = "",
        startDate = Date(),
        endDate = Date(),
        frequency = "",
        times = emptyList(),
        refillDays = 0,
        note = "",
        address = "",
        postalCode = "",
        postalCodeError = null,
        lastRefilledDate = Date(),
        owner = 0
    )
}