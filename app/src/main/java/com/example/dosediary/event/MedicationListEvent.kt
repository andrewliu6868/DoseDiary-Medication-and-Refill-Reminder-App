package com.example.dosediary.event

import com.example.dosediary.model.entity.Medication

sealed class MedicationListEvent {
    data class SelectMedication(val medication: Medication) : MedicationListEvent()
}