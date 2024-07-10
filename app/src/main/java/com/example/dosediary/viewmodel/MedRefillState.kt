package com.example.dosediary.viewmodel

import com.example.dosediary.model.Medication

data class MedRefillState(
    val medRefills: List<Medication> = emptyList()
)