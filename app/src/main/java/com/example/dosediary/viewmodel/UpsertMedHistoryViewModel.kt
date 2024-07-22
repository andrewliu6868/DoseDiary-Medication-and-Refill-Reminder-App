package com.example.dosediary.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dosediary.event.UpsertMedHistoryEvent
import com.example.dosediary.model.dao.MedicationHistoryDao
import com.example.dosediary.model.entity.Medication
import com.example.dosediary.model.entity.MedicationHistory
import com.example.dosediary.state.UpsertMedHistoryState
import com.example.dosediary.state.UpsertMedicationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpsertMedHistoryViewModel @Inject constructor(
    application: Application,
    private val medicationHistoryDao: MedicationHistoryDao
) : ViewModel() {
    private val _state = MutableStateFlow(UpsertMedHistoryState())
    val state = _state.asStateFlow()


    fun initialize(medication: MedicationHistory?) {
        if (medication != null) {
            _state.value = _state.value.copy(
                id = medication.id,
                name = medication.name,
                timeTaken = medication.timeTaken,
                dateTaken = medication.dateTaken,
                effectiveness = medication.effectiveness,
                ownerId = medication.ownerId,
                additionalDetails = medication.additionalDetails
            )
        } else {
            _state.value = UpsertMedHistoryState()
        }
    }


    fun onEvent(event: UpsertMedHistoryEvent) {
        when (event) {
            is UpsertMedHistoryEvent.OnMedicationNameChanged -> {
                _state.value = _state.value.copy(name = event.name)
            }

            is UpsertMedHistoryEvent.OnEffectivenessChanged -> {
                _state.value = _state.value.copy(effectiveness = event.effectiveness)
            }

            is UpsertMedHistoryEvent.OnDateChanged -> {
                _state.value = _state.value.copy(dateTaken = event.date)
            }

            is UpsertMedHistoryEvent.OnTimeChanged -> {
                _state.value = _state.value.copy(timeTaken = event.time)
            }

            is UpsertMedHistoryEvent.OnAdditionalDetailsChanged -> {
                _state.value = _state.value.copy(additionalDetails = event.details)
            }

            is UpsertMedHistoryEvent.OnSaveClicked -> {
                _state.value = _state.value.copy(showConfirmDialog = true)
            }

            is UpsertMedHistoryEvent.OnConfirmClicked -> {
                // Here you would handle saving the medication history
                saveMedicationHistory()
                _state.value = _state.value.copy(showConfirmDialog = false)
            }

            is UpsertMedHistoryEvent.OnConfirmDialogDismissed -> {
                _state.value = _state.value.copy(showConfirmDialog = false)
            }
        }
    }

    private fun saveMedicationHistory() {
        viewModelScope.launch {
            val medicationHistory = MedicationHistory(
                ownerId = _state.value.ownerId,
                name = _state.value.name,
                effectiveness = _state.value.effectiveness,
                dateTaken = _state.value.dateTaken,
                timeTaken = _state.value.timeTaken,
                additionalDetails = _state.value.additionalDetails
            )
            medicationHistoryDao.upsertMedicationHistory(medicationHistory)
        }
    }

        private fun UpsertMedHistoryState.toMedicationHistory(): MedicationHistory {
            return MedicationHistory(
                id = this.id,
                name = this.name,
                dateTaken = this.dateTaken,
                timeTaken = this.timeTaken,
                effectiveness = this.effectiveness,
                ownerId = this.ownerId,
                additionalDetails = this.additionalDetails
            )
        }

//    private fun resetInputFields() {
//        _state.value = _state.value.copy(
//            selectedMedicationHistory = null
//        )
//    }

}
