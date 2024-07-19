package com.example.dosediary.viewmodel

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.dosediary.utils.DoseDiaryDatabase
import kotlinx.coroutines.launch
import java.util.*
import androidx.lifecycle.ViewModel
import com.example.dosediary.model.entity.Medication
import com.example.dosediary.state.MedRefillDetailState
import com.example.dosediary.state.MedRefillState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MedRefillDetailViewModel @Inject constructor(
    application: Application
) : ViewModel(){
    private val medicationDao = DoseDiaryDatabase.getInstance(application).medicationDao

    //    private val _state = mutableStateOf<Medication?>(null)
    private val _state = MutableStateFlow(MedRefillDetailState())

    //    val state: State<Medication?> = _state
    val state = _state.asStateFlow()

    fun fetchMedById(id: Int) {
        viewModelScope.launch {
            medicationDao.getMedicationByID(id).collect { medication ->
                _state.value = MedRefillDetailState(medRefillsMedication = medication)
            }
        }
    }
}

fun showMedName (medicationName: String): String{
    return medicationName
}
fun showOwner (owner: String): String{
    return owner
}
fun showRefillDays(refillDays:Int): Int {
    return refillDays
}
fun showStartDate(startDate: Date): Date{
    return startDate
}