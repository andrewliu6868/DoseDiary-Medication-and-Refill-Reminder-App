package com.example.dosediary.events

import java.util.Date

sealed interface AddMedicationEvent {
    data class OnMedicationNameChanged(val medicationName: String) : AddMedicationEvent
    data class OnStartDateChanged(val startDate: Date) : AddMedicationEvent
    data class OnEndDateChanged(val endDate: Date) : AddMedicationEvent
    data class OnFrequencyChanged(val frequency: String) : AddMedicationEvent
    data class OnTimeChanged(val index: Int, val time: Date) : AddMedicationEvent
    data class OnRefillDaysChanged(val refillDays: Int) : AddMedicationEvent
    data class OnNoteChanged(val note: String) : AddMedicationEvent
    data class OnAddressChanged(val address: String) : AddMedicationEvent
    data class OnPostalCodeChanged(val postalCode: String) : AddMedicationEvent
    object SaveMedication : AddMedicationEvent
    object ConfirmSaveMedication : AddMedicationEvent // New event
    object DismissDialog : AddMedicationEvent // New event
}