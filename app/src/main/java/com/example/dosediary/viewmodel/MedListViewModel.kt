package com.example.dosediary.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dosediary.event.MedicationListEvent
import com.example.dosediary.model.dao.MedicationDao
import com.example.dosediary.model.entity.Medication
import com.example.dosediary.state.MedicationListState
import com.example.dosediary.state.UpsertMedicationState
import com.example.dosediary.state.UserState
import com.example.dosediary.utils.DoseDiaryDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MedicationListViewModel @Inject constructor(
    application: Application,
) : ViewModel() {
    private val medicationDao = DoseDiaryDatabase.getInstance(application).medicationDao
    private val _state = MutableStateFlow(MedicationListState())
    val state: StateFlow<MedicationListState> = _state

    fun initialize(userId: Int) {
        viewModelScope.launch {
            medicationDao.getMedicationOrderedByFirstName(userId).collect { medications ->
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
}