package com.example.dosediary.event

import com.example.dosediary.events.MedicationHistoryEvent
import com.example.dosediary.model.entity.MedicationHistory

sealed interface EditMedHistoryEvent {
    data class OnEffectivenessChanged(val effectiveness: String) : EditMedHistoryEvent
    data class AddMedicationHistory(val medicationHistory: MedicationHistory) : EditMedHistoryEvent
    data class OnAdditionalDetailsChanged(val additionalDetails: String) : EditMedHistoryEvent

    object FetchMedicationHistories : EditMedHistoryEvent
    object SaveMedicationHistory: EditMedHistoryEvent
    object ConfirmSaveMedHistory: EditMedHistoryEvent
}