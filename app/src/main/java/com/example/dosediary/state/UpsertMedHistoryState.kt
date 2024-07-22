package com.example.dosediary.state

import com.example.dosediary.model.entity.MedicationHistory

data class UpsertMedHistoryState (
//    val medicationHistories: List<MedicationHistory> = emptyList(),
    val showConfirmDialog: Boolean = false,

    val id: Int = 0,
    val name: String = "",
    val timeTaken: String = "",
    val dateTaken: String = "",
    val effectiveness: String = "",
    val ownerId: Int = 0,
    val additionalDetails: String = ""
)
