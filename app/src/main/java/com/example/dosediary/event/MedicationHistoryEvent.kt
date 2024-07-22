package com.example.dosediary.events

import com.example.dosediary.event.MedicationListEvent
import com.example.dosediary.model.entity.Medication
import com.example.dosediary.model.entity.MedicationHistory

sealed interface MedicationHistoryEvent {
    object FetchMedicationHistories : MedicationHistoryEvent
    data class DeleteMedicationHistory(val medicationHistory: MedicationHistory) : MedicationHistoryEvent
    data class AddOrUpdateMedicationHistory(val medicationHistory: MedicationHistory) : MedicationHistoryEvent

    data class SelectMedication(val medicationHistory: MedicationHistory?) : MedicationHistoryEvent
}