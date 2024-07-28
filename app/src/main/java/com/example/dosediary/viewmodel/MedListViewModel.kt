package com.example.dosediary.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.dosediary.event.MedicationListEvent
import com.example.dosediary.model.dao.MedicationDao
import com.example.dosediary.model.entity.Medication
import com.example.dosediary.model.entity.User
import com.example.dosediary.state.MedicationListState
import com.example.dosediary.state.UserState
import com.example.dosediary.utils.DoseDiaryDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class MedicationListViewModel @Inject constructor(
    application: Application,
) : ViewModel() {
    private val medicationDao = DoseDiaryDatabase.getInstance(application).medicationDao
//    private val _currentUser: MutableStateFlow<User?> = userState.currentUser

    private val _state = MutableStateFlow(MedicationListState())
    val state: StateFlow<MedicationListState> = _state.asStateFlow()

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

    fun updateTakenStatus(medicationId: Int, date: Date, taken: Boolean) {
        viewModelScope.launch {
            val medication = medicationDao.getMedicationByID(medicationId).first()
            val updatedTakenTimes = medication.takenTimes.toMutableMap()
            updatedTakenTimes[date] = taken
            val updatedMedication = medication.copy(takenTimes = updatedTakenTimes)
            medicationDao.upsertMedication(updatedMedication)
        }
    }
}

//class MedicationListViewModelFactory(private val application: Application, private val userState: UserState) : ViewModelProvider.Factory{
//    override fun <T: ViewModel> create(modelClass: Class<T>): T{
//        if(modelClass.isAssignableFrom(MedicationListViewModel::class.java)){
//            return MedicationListViewModel(userState, application) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}
