package com.example.dosediary.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dosediary.events.MedicationHistoryEvent
import com.example.dosediary.model.dao.MedicationHistoryDao
import com.example.dosediary.model.entity.MedicationHistory
import com.example.dosediary.state.MedicationHistoryState
import com.example.dosediary.state.UserState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MedicationHistoryViewModel @Inject constructor(
    private val medicationHistoryDao: MedicationHistoryDao,
    private val userState: UserState
) : ViewModel() {

    private val _state = MutableStateFlow(MedicationHistoryState())
    val state = _state.asStateFlow()

    init {
        addTestEntries()
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
            val ownerId = 1
            medicationHistoryDao.getMedicationHistoriesByOwner(ownerId).collect { histories ->
                _state.value = _state.value.copy(medicationHistories = histories)
            }
        }
    }

    private fun addOrUpdateMedicationHistory(medicationHistory: MedicationHistory) {
        viewModelScope.launch {
            medicationHistoryDao.upsertMedicationHistory(medicationHistory)
            fetchMedicationHistories()
        }
    }

    private fun deleteMedicationHistory(medicationHistory: MedicationHistory) {
        viewModelScope.launch {
            medicationHistoryDao.deleteMedicationHistory(medicationHistory)
            fetchMedicationHistories()
        }
    }

    fun addTestEntries() {
        viewModelScope.launch {
            val ownerId = 1
            val testEntries = listOf(
                MedicationHistory(name = "Ibuprofen", timeTaken = "8:00 AM", dateTaken = "2024-07-15", effectiveness = "Effective", ownerId = ownerId),
                MedicationHistory(name = "Aspirin", timeTaken = "12:00 PM", dateTaken = "2024-07-15", effectiveness = "Moderate", ownerId = ownerId),
                MedicationHistory(name = "Paracetamol", timeTaken = "6:00 PM", dateTaken = "2024-07-15", effectiveness = "Effective", ownerId = ownerId)
            )
            testEntries.forEach {
                medicationHistoryDao.upsertMedicationHistory(it)
            }
            fetchMedicationHistories()
        }
    }
}
