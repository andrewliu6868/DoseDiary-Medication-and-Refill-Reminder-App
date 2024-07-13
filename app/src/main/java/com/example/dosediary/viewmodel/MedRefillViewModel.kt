package com.example.dosediary.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.dosediary.model.DoseDiaryDatabase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MedRefillViewModel(application: Application): ViewModel() {

    private val medicationDao = DoseDiaryDatabase.getInstance(application).medicationDao

    private val _user = "User 1"
    private val _medications = medicationDao.getMedicationsByOwner(_user)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _state = MutableStateFlow(MedRefillState())

    val state = combine(_state, _medications) { state, medications ->
        state.copy(
            medRefills = medications
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), MedRefillState())

    fun onEvent(event: MedRefillEvent) {
        when(event) {
            is MedRefillEvent.SetRefillCompleted -> {
                viewModelScope.launch {
//                    val medRefills = medicationDao.getMedications()
//                    _state.value = MedRefillState(medRefills)
                }
            }
        }
    }

}

class MedRefillViewModelFactory(private val application: Application) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(MedRefillViewModel::class.java)) {
            return MedRefillViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}