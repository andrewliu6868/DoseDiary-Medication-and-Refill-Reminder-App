package com.example.dosediary.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dosediary.event.MedicationListEvent
import com.example.dosediary.model.dao.MedicationDao
import com.example.dosediary.model.entity.Medication
import com.example.dosediary.state.MedicationListState
import com.example.dosediary.utils.DoseDiaryDatabase
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class MedicationListViewModel @Inject constructor(
    application: Application
) : ViewModel() {
    private val medicationDao = DoseDiaryDatabase.getInstance(application).medicationDao
    private val _state = MutableStateFlow(MedicationListState())
    val state: StateFlow<MedicationListState> = _state

    init {
        viewModelScope.launch {
            medicationDao.getActiveMedicationsOrderedByName(System.currentTimeMillis()).collect { medications ->
                _state.value = _state.value.copy(medicationList = medications)
            }
        }
    }

    fun onEvent(event: MedicationListEvent) {
        when (event) {
            is MedicationListEvent.SelectMedication -> {
                _state.value = _state.value.copy(selectedMedicationDetail = event.medication)
            }
        }
    }

    fun updateTakenStatus(medicationId: Int, date: Date, taken: Boolean) {
        viewModelScope.launch {
            val medication = medicationDao.getMedicationByID(medicationId).first()
            val updatedTakenTimes = medication.takenTimes.toMutableMap().apply {
                this[date] = taken
            }
            val gson = Gson()
            val takenTimesJson = gson.toJson(updatedTakenTimes.mapKeys { it.key.time })
            medicationDao.updateTakenState(medication.id, takenTimesJson)
        }
    }
}
