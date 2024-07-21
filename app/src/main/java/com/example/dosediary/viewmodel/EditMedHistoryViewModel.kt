package com.example.dosediary.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dosediary.event.EditMedHistoryEvent
import com.example.dosediary.model.dao.MedicationHistoryDao
import com.example.dosediary.model.entity.MedicationHistory
import com.example.dosediary.state.EditMedHistoryState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditMedHistoryViewModel @Inject constructor(
    application: Application,
    private val medicationHistoryDao: MedicationHistoryDao
) : ViewModel() {
    private val _state = MutableStateFlow(EditMedHistoryState())
    val state = _state.asStateFlow()

    init {
        fetchMedicationHistories()
    }

    fun onEvent(event: EditMedHistoryEvent) {
        when (event) {
            is EditMedHistoryEvent.OnEffectivenessChanged -> {
                _state.value = _state.value.copy(effectiveness = event.effectiveness)
            }
            is EditMedHistoryEvent.AddMedicationHistory -> {
                addMedicationHistory(event.medicationHistory)
            }
            EditMedHistoryEvent.FetchMedicationHistories -> {
                fetchMedicationHistories()
            }
            EditMedHistoryEvent.SaveMedicationHistory -> {
                saveMedicationHistory()
            }
            EditMedHistoryEvent.ConfirmSaveMedHistory -> {
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
                val updatedHistory = selectedHistory.copy(effectiveness = _state.value.effectiveness)
                medicationHistoryDao.upsertMedicationHistory(updatedHistory)
                fetchMedicationHistories()
                resetInputFields()
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
