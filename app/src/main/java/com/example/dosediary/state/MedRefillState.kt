package com.example.dosediary.state

import com.example.dosediary.model.entity.Medication
import com.google.android.gms.maps.model.LatLng
import java.util.Date

data class MedicationWithNextRefillDate(
    val medication: Medication,
    val nextRefillDate: Date?
)

data class MedRefillState(
    val medRefillsToday: List<MedicationWithNextRefillDate> = emptyList(),
    val medRefillsUpcoming: List<MedicationWithNextRefillDate> = emptyList(),
    val selectedRefillDetail: MedicationWithNextRefillDate = MedicationWithNextRefillDate(defaultMedication(), null),
    val showRefillConfirmationDialog: Boolean = false,
    val selectedRefilledMedication: MedicationWithNextRefillDate = MedicationWithNextRefillDate(defaultMedication(), null)
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
        addressLatLng = LatLng(0.0, 0.0),
        lastRefilledDate = Date(),
        owner = 0
    )
}