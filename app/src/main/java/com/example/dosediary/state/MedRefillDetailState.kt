package com.example.dosediary.state

import com.example.dosediary.model.entity.Medication
import java.util.Date

data class MedRefillDetailState(
    val medRefillsMedication: Medication = Medication(
        medicationName = "",
        startDate = Date(),
        endDate = Date(),
        frequency = "",
        //times = emptyList(),
        refillDays = 0,
        note = "",
        address = "",
        postalCode = "",
        postalCodeError = null,
        lastRefilledDate = Date(),
        owner = 0
    )
)