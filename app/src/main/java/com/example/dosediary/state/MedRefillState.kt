package com.example.dosediary.state

import com.example.dosediary.model.entity.Medication

data class MedRefillState(
    val medRefillsToday: List<Medication> = emptyList(),
    val medRefillsUpcoming: List<Medication> = emptyList()

)