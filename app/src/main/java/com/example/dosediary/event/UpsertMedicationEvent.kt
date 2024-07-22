package com.example.dosediary.events

import com.example.dosediary.state.AutocompleteResult
import java.util.Date

sealed interface UpsertMedicationEvent {
    data class OnMedicationNameChanged(val medicationName: String) : UpsertMedicationEvent
    data class OnStartDateChanged(val startDate: Date) : UpsertMedicationEvent
    data class OnEndDateChanged(val endDate: Date) : UpsertMedicationEvent
    data class OnFrequencyChanged(val frequency: String) : UpsertMedicationEvent
    data class OnTimeChanged(val index: Int, val time: Date) : UpsertMedicationEvent
    data class OnRefillDaysChanged(val refillDays: Int) : UpsertMedicationEvent
    data class OnNoteChanged(val note: String) : UpsertMedicationEvent
    data class OnAddressChanged(val address: String) : UpsertMedicationEvent
    object SaveMedication : UpsertMedicationEvent
    object ConfirmSaveMedication : UpsertMedicationEvent
    object DismissSaveDialog : UpsertMedicationEvent
    object DeleteMedication : UpsertMedicationEvent
    object ConfirmDeleteMedication : UpsertMedicationEvent
    object DismissDeleteDialog : UpsertMedicationEvent

    data class OnClickWithRipple(val autoCompleteResult: AutocompleteResult) : UpsertMedicationEvent
}
