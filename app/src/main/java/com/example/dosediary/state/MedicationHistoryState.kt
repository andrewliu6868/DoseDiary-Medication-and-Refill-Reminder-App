package com.example.dosediary.state

import com.example.dosediary.model.entity.Medication
import com.example.dosediary.model.entity.MedicationHistory

data class MedicationHistoryState(
    val medicationHistories: List<MedicationHistory> = emptyList(),
    val selectedMedicationHistory: MedicationHistory? = null
)
