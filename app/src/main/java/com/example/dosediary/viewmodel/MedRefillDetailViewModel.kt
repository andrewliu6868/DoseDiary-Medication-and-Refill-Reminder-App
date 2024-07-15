package com.example.dosediary.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.dosediary.model.DoseDiaryDatabase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import androidx.lifecycle.ViewModel
import com.example.dosediary.model.MedicationDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MedRefillDetailViewModel(application: Application) : ViewModel(){
    private val medicationDao = DoseDiaryDatabase.getInstance(application).medicationDao

    private val _state = medicationDao.getMedicationByID(1) // pass in id

    fun onEvent(event: MedRefillDetailEvent){
        when(event){
            is MedRefillDetailEvent.clickedMedication ->{
                viewModelScope.launch{

                }
            }
        }
    }

}