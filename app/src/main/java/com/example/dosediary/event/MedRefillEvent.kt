package com.example.dosediary.event

import com.example.dosediary.state.MedicationWithNextRefillDate

sealed interface MedRefillEvent {
    data class SetRefillCompleted(val medicationWithNextRefillDate: MedicationWithNextRefillDate) : MedRefillEvent
}