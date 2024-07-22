package com.example.dosediary.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dosediary.event.UpsertMedHistoryEvent
import com.example.dosediary.model.dao.MedicationHistoryDao
import com.example.dosediary.model.entity.MedicationHistory
import com.example.dosediary.state.UpsertMedHistoryState
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

    init {
        fetchMedicationHistories()
    }

    fun onEvent(event: UpsertMedHistoryEvent) {
        when (event) {
            is UpsertMedHistoryEvent.OnEffectivenessChanged -> {
                _state.value = _state.value.copy(effectiveness = event.effectiveness)
            }
            is UpsertMedHistoryEvent.OnAdditionalDetailsChanged -> {
                _state.value = _state.value.copy(additionalDetails = event.additionalDetails)
            }
            is UpsertMedHistoryEvent.AddMedicationHistory -> {
                addMedicationHistory(event.medicationHistory)
            }
            UpsertMedHistoryEvent.FetchMedicationHistories -> {
                fetchMedicationHistories()
            }
            UpsertMedHistoryEvent.SaveMedicationHistory -> {
                saveMedicationHistory()
            }
            UpsertMedHistoryEvent.ConfirmSaveMedHistory -> {
                _state.value = _state.value.copy(showConfirmDialog = true)
            }
        }
    }

    private fun fetchMedicationHistories() {
        viewModelScope.launch {
            medicationHistoryDao.getContactsOrderedByFirstName().collect { histories ->
                _state.value = _state.value.copy(medicationHistories = histories)
            }
        }
    }

    private fun addMedicationHistory(medicationHistory: MedicationHistory) {
        viewModelScope.launch {
            medicationHistoryDao.upsertMedicationHistory(medicationHistory)
            fetchMedicationHistories()
        }
    }

    private fun saveMedicationHistory() {
        viewModelScope.launch {
            val selectedHistory = _state.value.selectedMedicationHistory
            if (selectedHistory != null) {
                val updatedHistory = selectedHistory.copy(
                    effectiveness = _state.value.effectiveness,
                    additionalDetails = _state.value.additionalDetails
                )
                medicationHistoryDao.upsertMedicationHistory(updatedHistory)
                fetchMedicationHistories()
                _state.value = _state.value.copy(selectedMedicationHistory = null, effectiveness = "", additionalDetails = "")
            }
        }
    }

    private fun resetInputFields() {
        _state.value = _state.value.copy(
            selectedMedicationHistory = null,
            effectiveness = "",
            additionalDetails = "",
            showConfirmDialog = false
        )
    }
}
