package com.example.dosediary.event

import com.example.dosediary.model.entity.MedicationHistory

sealed interface UpsertMedHistoryEvent {
    data class OnMedicationNameChanged(val name: String) : UpsertMedHistoryEvent
    data class OnEffectivenessChanged(val effectiveness: String) : UpsertMedHistoryEvent
    data class OnDateChanged(val date: String) : UpsertMedHistoryEvent
    data class OnTimeChanged(val time: String) : UpsertMedHistoryEvent
    data class OnAdditionalDetailsChanged(val details: String) : UpsertMedHistoryEvent
    object OnSaveClicked : UpsertMedHistoryEvent
    object OnConfirmClicked : UpsertMedHistoryEvent
    object OnConfirmDialogDismissed : UpsertMedHistoryEvent
}