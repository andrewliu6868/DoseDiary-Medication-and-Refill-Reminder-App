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

class MedRefillDetailViewModel(application: Application) : ViewModel(){
    private val medicationDao = DoseDiaryDatabase.getInstance(application).medicationDao

    private val _state = mutableStateOf<Medication?>(null)

    val state: State<Medication?> = _state
    fun fetchMedById(id: Int) {
        viewModelScope.launch {
            medicationDao.getMedicationByID(id).collect { medication ->
                _state.value = medication
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


class MedRefillDetailViewModelFactory(private val application: Application) : ViewModelProvider.Factory{
    override fun <T: ViewModel> create(modelClass: Class<T>): T{
        if(modelClass.isAssignableFrom(MedRefillDetailViewModel::class.java)){
            return MedRefillDetailViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}