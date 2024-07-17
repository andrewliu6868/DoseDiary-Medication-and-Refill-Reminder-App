package com.example.dosediary.viewmodel

import android.app.Application
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.dosediary.model.DoseDiaryDatabase
import com.example.dosediary.model.User
import com.example.dosediary.model.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MedRefillViewModel  @Inject constructor(
    private val userRepository: UserRepository,
    application: Application
): ViewModel() {

    private val medicationDao = DoseDiaryDatabase.getInstance(application).medicationDao

    private val _currentUser: StateFlow<User?> = userRepository.users
    private val _medications = medicationDao.getMedicationsByOwner(1)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _state = MutableStateFlow(MedRefillState())

    val state = combine(_state, _medications) { state, medications ->
        state.copy(
            medRefillsToday = medications.filter { medication ->

                calculateRefillDates(medication.startDate, medication.endDate, medication.refillDays).any { refillDate ->
                    needsRefillToday(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(refillDate))
                }
            },
//            medRefillsToday = _medications.value,
            medRefillsUpcoming = medications.filter { medication ->
                calculateRefillDates(
                    medication.startDate,
                    medication.endDate,
                    medication.refillDays
                ).any { refillDate ->
                    needsRefillNextWeek(
                        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                            refillDate
                        )
                    )
                }
            }
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

fun calculateRefillDates(startDate: Date, endDate: Date, refillDays: Int): List<Date> {
    val refillDates = mutableListOf<Date>()
    val calendar = Calendar.getInstance()
    calendar.time = startDate

    while (!calendar.time.after(endDate)) {
        refillDates.add(calendar.time)
        calendar.add(Calendar.DAY_OF_YEAR, refillDays)
    }

    return refillDates
}


fun needsRefillToday(refillDate: String): Boolean {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val today = Calendar.getInstance().time
    return sdf.format(today) == refillDate
}

fun needsRefillNextWeek(refillDate: String): Boolean {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val today = Calendar.getInstance()
    val nextWeek = Calendar.getInstance()
    nextWeek.add(Calendar.DAY_OF_YEAR, 7)
    val refill = Calendar.getInstance()
    sdf.parse(refillDate)?.let {
        refill.time = it
    }
    return refill.after(today) && refill.before(nextWeek)
}

class MedRefillViewModelFactory(private val application: Application, private val userRepository: UserRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(MedRefillViewModel::class.java)) {
            return MedRefillViewModel(userRepository, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}