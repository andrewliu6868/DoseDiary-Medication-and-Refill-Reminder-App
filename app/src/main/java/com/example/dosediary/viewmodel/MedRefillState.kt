package com.example.dosediary.viewmodel

import com.example.dosediary.model.Medication

data class MedRefillState(
    val medRefillsToday: List<Medication> = emptyList(),
    val medRefillsUpcoming: List<Medication> = emptyList()

)