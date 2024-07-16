package com.example.dosediary.viewmodel

import androidx.annotation.Nullable
import com.example.dosediary.model.Medication
data class MedRefillDetailState(
    val medRefillsMedication: Medication = Medication()
)