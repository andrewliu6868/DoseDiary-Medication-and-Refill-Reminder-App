package com.example.dosediary.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dosediary.events.MedicationHistoryEvent
import com.example.dosediary.model.entity.MedicationHistory
import com.example.dosediary.model.dao.MedicationHistoryDao
import com.example.dosediary.state.MedicationHistoryState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MedicationHistoryViewModel @Inject constructor(
    private val medicationHistoryDao: MedicationHistoryDao
) : ViewModel() {

    private val _state = MutableStateFlow(MedicationHistoryState())
    val state = _state.asStateFlow()

    init {
        fetchMedicationHistories()
    }

    fun onEvent(event: MedicationHistoryEvent) {
        when (event) {
            is MedicationHistoryEvent.FetchMedicationHistories -> {
                fetchMedicationHistories()
            }
            is MedicationHistoryEvent.DeleteMedicationHistory -> {
                deleteMedicationHistory(event.medicationHistory)
            }
            is MedicationHistoryEvent.AddOrUpdateMedicationHistory -> {
                addOrUpdateMedicationHistory(event.medicationHistory)
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

    private fun addOrUpdateMedicationHistory(medicationHistory: MedicationHistory) {
        viewModelScope.launch {
            medicationHistoryDao.upsertMedicationHistory(medicationHistory)
        }
    }

    private fun deleteMedicationHistory(medicationHistory: MedicationHistory) {
        viewModelScope.launch {
            medicationHistoryDao.deleteMedicationHistory(medicationHistory)
        }
    }

    fun addTestEntries() {
        viewModelScope.launch {
            val testEntries = listOf(
                MedicationHistory(name = "Ibuprofen", timeTaken = "8:00 AM", dateTaken = "2024-07-15", effectiveness = "Effective"),
                MedicationHistory(name = "Aspirin", timeTaken = "12:00 PM", dateTaken = "2024-07-15", effectiveness = "Moderate"),
                MedicationHistory(name = "Paracetamol", timeTaken = "6:00 PM", dateTaken = "2024-07-15", effectiveness = "Effective")
            )
            testEntries.forEach {
                medicationHistoryDao.upsertMedicationHistory(it)
            }
        }
    }
}
