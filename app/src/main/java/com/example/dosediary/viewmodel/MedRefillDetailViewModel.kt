package com.example.dosediary.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dosediary.model.MedicationDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MedRefillDetailViewModel(
    private val medicationDao: MedicationDao
) : ViewModel(){
    private val _state = MutableStateFlow(MedRefillState())
    val state = _state.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), MedRefillState())

    fun onEvent(event: MedRefillDetailEvent){
        when(event){
            is MedRefillDetailEvent.clickedMedication ->{
                viewModelScope.launch{

                }
            }
        }
    }

}