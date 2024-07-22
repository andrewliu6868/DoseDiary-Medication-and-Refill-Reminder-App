package com.example.dosediary.state

import com.example.dosediary.model.entity.Medication

data class MedicationListState (
    val medicationList: List<Medication> = emptyList(),
    val selectedMedicationDetail: Medication? = null
)

