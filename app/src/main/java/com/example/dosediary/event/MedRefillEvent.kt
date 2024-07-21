package com.example.dosediary.event

import com.example.dosediary.model.entity.Medication
import com.example.dosediary.state.MedicationWithNextRefillDate

sealed interface MedRefillEvent {
    data class SetRefillCompleted(val medicationWithNextRefillDate: MedicationWithNextRefillDate) : MedRefillEvent
    data class SetSelectedRefillDetail (val medicationWithNextRefillDate: MedicationWithNextRefillDate) : MedRefillEvent
}