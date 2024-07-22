package com.example.dosediary.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dosediary.event.MedRefillEvent
import com.example.dosediary.model.entity.Medication
import com.example.dosediary.utils.DoseDiaryDatabase
import com.example.dosediary.model.entity.User
import com.example.dosediary.state.UserState
import com.example.dosediary.state.MedRefillState
import com.example.dosediary.state.MedicationWithNextRefillDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class MedRefillViewModel  @Inject constructor(
    private val userState: UserState,
    application: Application
): ViewModel() {
    private val medicationDao = DoseDiaryDatabase.getInstance(application).medicationDao
//    private val _currentUser: MutableStateFlow<User?> = userState.currentUser
    private val userDao = DoseDiaryDatabase.getInstance(application).userDao
    private val _currentUser: StateFlow<User?> = userDao.getUserById(1).stateIn(viewModelScope, SharingStarted.WhileSubscribed(), User())

    private val _state = MutableStateFlow(MedRefillState())
    val state: StateFlow<MedRefillState> = _state.asStateFlow()
    init {
        viewModelScope.launch {
            _currentUser.flatMapLatest { currentUser ->
                val medicationsFlow = if (currentUser != null) {
                    medicationDao.getMedicationsByOwner(currentUser.id)
                } else {
                    flowOf(emptyList())
                }

                combine(medicationsFlow, _currentUser) { medications, user ->
                    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val today = sdf.format(Calendar.getInstance().time)
                    val nextWeek = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 7) }
                    val nextWeekFormatted = sdf.format(nextWeek.time)

                    val medRefillsToday = medications.mapNotNull { medication ->
                        calculateNextRefillDate(medication)?.let { refillDate ->
                            if (sdf.format(refillDate) == today) MedicationWithNextRefillDate(medication, refillDate) else null
                        }
                    }

                    val medRefillsUpcoming = medications.mapNotNull { medication ->
                        calculateNextRefillDate(medication)?.let { refillDate ->
                            val refillDateFormatted = sdf.format(refillDate)
                            if (refillDateFormatted > today && refillDateFormatted <= nextWeekFormatted) MedicationWithNextRefillDate(medication, refillDate) else null
                        }
                    }

                    MedRefillState(
                        medRefillsToday = medRefillsToday,
                        medRefillsUpcoming = medRefillsUpcoming
                    )
                }
            }.collect { state ->
                _state.value = state
            }
        }
    }

    fun onEvent(event: MedRefillEvent) {
        when(event) {
            is MedRefillEvent.SetRefillCompleted -> {
                viewModelScope.launch {
                    medicationDao.updateLastRefillDate(
                        event.medicationWithNextRefillDate.medication.id,
                        event.medicationWithNextRefillDate.nextRefillDate ?: Date()
                    )
                }
            }
            is MedRefillEvent.SetSelectedRefillDetail -> {
                _state.value = _state.value.copy(
                    selectedRefillDetail = event.medicationWithNextRefillDate
                )
            }
        }
    }
}


fun calculateNextRefillDate(medication: Medication): Date? {
    val calendar = Calendar.getInstance().apply { time = medication.lastRefilledDate }

    // Check if last refilled date is beyond the end date
    if (medication.lastRefilledDate.after(medication.endDate)) {
        return null
    }
    // Loop to find the next refill date within the valid period
    while (calendar.time.before(medication.endDate)) {
        calendar.add(Calendar.DAY_OF_YEAR, medication.refillDays)
        if (calendar.time.after(medication.lastRefilledDate) && calendar.time.before(medication.endDate)) {
            return calendar.time
        }
    }
    return null
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