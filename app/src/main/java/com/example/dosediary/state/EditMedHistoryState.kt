package com.example.dosediary.state

import com.example.dosediary.model.entity.MedicationHistory

data class EditMedHistoryState (
    val medicationHistories: List<MedicationHistory> = emptyList(),
    val selectedMedicationHistory: MedicationHistory? = null,
    val effectiveness: String = "",
    val additionalDetails: String = "",
    val showConfirmDialog: Boolean = false,
)
