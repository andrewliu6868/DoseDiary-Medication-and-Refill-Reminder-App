package com.example.dosediary.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.dosediary.event.MedicationListEvent
import com.example.dosediary.events.MedicationHistoryEvent
import com.example.dosediary.model.dao.MedicationHistoryDao
import com.example.dosediary.model.entity.MedicationHistory
import com.example.dosediary.model.entity.User
import com.example.dosediary.state.MedicationHistoryState
import com.example.dosediary.state.UserState
import com.example.dosediary.utils.DoseDiaryDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MedicationHistoryViewModel @Inject constructor(
    private val userState: UserState,
    application: Application
) : ViewModel() {
    private val medicationHistoryDao: MedicationHistoryDao = DoseDiaryDatabase.getInstance(application).medicationHistoryDao
//    private val _currentUser: MutableStateFlow<User?> = userState.currentUser

    private val _state = MutableStateFlow(MedicationHistoryState())
    val state = _state.asStateFlow()

    init {
//        addTestEntries()
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
            is MedicationHistoryEvent.SelectMedication -> {
                _state.value = _state.value.copy(selectedMedicationHistory = event.medicationHistory)
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

    fun addOrUpdateMedicationHistory(medicationHistory: MedicationHistory) {
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
}

class MedicationHistoryViewModelFactory(private val application: Application, private val userState: UserState) : ViewModelProvider.Factory{
    override fun <T: ViewModel> create(modelClass: Class<T>): T{
        if(modelClass.isAssignableFrom(MedicationHistoryViewModel::class.java)){
            return MedicationHistoryViewModel(userState, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

