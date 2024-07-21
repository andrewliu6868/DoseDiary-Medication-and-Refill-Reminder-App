package com.example.dosediary.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dosediary.state.AddMedicationState
import com.example.dosediary.events.AddMedicationEvent
import com.example.dosediary.state.UserState
import com.example.dosediary.model.entity.Medication
import com.example.dosediary.utils.DoseDiaryDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class AddMedicationViewModel @Inject constructor(
    application: Application,
    private val userState: UserState
) : ViewModel() {
    private val medicationDao = DoseDiaryDatabase.getInstance(application).medicationDao
    private val _state = MutableStateFlow(AddMedicationState())
    val state = _state.asStateFlow()


    fun initialize(medication: Medication?) {
        if (medication != null) {
            _state.value = _state.value.copy(
                medicationId = medication.id,
                medicationName = medication.medicationName,
                startDate = medication.startDate,
                endDate = medication.endDate,
                frequency = medication.frequency,
                times = medication.times,
                refillDays = medication.refillDays,
                note = medication.note,
                address = medication.address,
                postalCode = medication.postalCode
            )
        }
    }

    fun onEvent(event: AddMedicationEvent) {
        when (event) {
            is AddMedicationEvent.OnMedicationNameChanged -> {
                _state.value = _state.value.copy(medicationName = event.medicationName)
            }
            is AddMedicationEvent.OnStartDateChanged -> {
                _state.value = _state.value.copy(startDate = event.startDate)
            }
            is AddMedicationEvent.OnEndDateChanged -> {
                _state.value = _state.value.copy(endDate = event.endDate)
            }
            is AddMedicationEvent.OnFrequencyChanged -> {
                val frequency = event.frequency
                val intVal = frequency.toIntOrNull()
                if (intVal != null && intVal > 0) {
                    val times = _state.value.times.toMutableList()
                    while (times.size < intVal) {
                        times.add(Date())
                    }
                    while (times.size > intVal) {
                        times.removeAt(times.size - 1)
                    }
                    _state.value = _state.value.copy(frequency = frequency, times = times)
                } else {
                    _state.value = _state.value.copy(frequency = frequency)
                }
            }
            is AddMedicationEvent.OnTimeChanged -> {
                val times = _state.value.times.toMutableList()
                times[event.index] = event.time
                _state.value = _state.value.copy(times = times)
            }
            is AddMedicationEvent.OnRefillDaysChanged -> {
                _state.value = _state.value.copy(refillDays = event.refillDays)
            }
            is AddMedicationEvent.OnNoteChanged -> {
                _state.value = _state.value.copy(note = event.note)
            }
            is AddMedicationEvent.OnAddressChanged -> {
                _state.value = _state.value.copy(address = event.address)
            }
            is AddMedicationEvent.OnPostalCodeChanged -> {
                val postalCodeRegex = Regex("^[A-Za-z]\\d[A-Za-z][ -]?\\d[A-Za-z]\\d$")
                val postalCodeError = if (postalCodeRegex.matches(event.postalCode)) null else "Invalid postal code format"
                _state.value = _state.value.copy(postalCode = event.postalCode, postalCodeError = postalCodeError)
            }
            AddMedicationEvent.SaveMedication -> {
                _state.value = _state.value.copy(showConfirmDialog = true)
            }
            AddMedicationEvent.ConfirmSaveMedication -> {
                viewModelScope.launch {
                    val medication = _state.value.toMedication()
                    medicationDao.upsertMedication(medication)
                    _state.value = AddMedicationState()
                }
            }
            AddMedicationEvent.DismissSaveDialog -> {
                _state.value = _state.value.copy(showConfirmDialog = false)
            }
            AddMedicationEvent.DeleteMedication -> {
                _state.value = _state.value.copy(showDeleteConfirmDialog = true)
            }
            AddMedicationEvent.ConfirmDeleteMedication -> {
                viewModelScope.launch {
                    medicationDao.deleteMedicationById(_state.value.medicationId)
                    _state.value = AddMedicationState()
                }
            }
            AddMedicationEvent.DismissDeleteDialog -> {
                _state.value = _state.value.copy(showDeleteConfirmDialog = false)
            }
        }
    }

    private fun AddMedicationState.toMedication(): Medication {
        return Medication(
            id = this.medicationId,
            medicationName = this.medicationName,
            startDate = this.startDate,
            endDate = this.endDate,
            frequency = this.frequency,
            times = this.times,
            refillDays = this.refillDays,
            note = this.note,
            address = this.address,
            postalCode = this.postalCode,
            postalCodeError = this.postalCodeError,
            lastRefilledDate = this.startDate,
            owner = 1  // TODO: Change this after User Profile is Ready
        )
    }

}
