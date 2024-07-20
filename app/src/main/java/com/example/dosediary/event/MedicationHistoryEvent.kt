package com.example.dosediary.events

import com.example.dosediary.model.entity.MedicationHistory

sealed class MedicationHistoryEvent {
    object FetchMedicationHistories : MedicationHistoryEvent()
    data class DeleteMedicationHistory(val medicationHistory: MedicationHistory) : MedicationHistoryEvent()
    data class AddOrUpdateMedicationHistory(val medicationHistory: MedicationHistory) : MedicationHistoryEvent()
}
