package com.example.dosediary.event

import com.example.dosediary.model.entity.MedicationHistory

sealed interface UpsertMedHistoryEvent {
    data class OnEffectivenessChanged(val effectiveness: String) : UpsertMedHistoryEvent
    data class AddMedicationHistory(val medicationHistory: MedicationHistory) : UpsertMedHistoryEvent
    data class OnAdditionalDetailsChanged(val additionalDetails: String) : UpsertMedHistoryEvent

    object FetchMedicationHistories : UpsertMedHistoryEvent
    object SaveMedicationHistory: UpsertMedHistoryEvent
    object ConfirmSaveMedHistory: UpsertMedHistoryEvent
}