package com.example.dosediary.state

import com.example.dosediary.model.entity.Medication
import java.util.Date

data class MedicationWithNextRefillDate(
    val medication: Medication,
    val nextRefillDate: Date?
)

data class MedRefillState(
    val medRefillsToday: List<MedicationWithNextRefillDate> = emptyList(),
    val medRefillsUpcoming: List<MedicationWithNextRefillDate> = emptyList()
)