package com.example.dosediary.state

import com.example.dosediary.model.entity.Medication
data class MedRefillDetailState(
    val medRefillsMedication: Medication = Medication()
)