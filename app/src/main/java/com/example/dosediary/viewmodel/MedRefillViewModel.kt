package com.example.dosediary.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.dosediary.event.MedRefillEvent
import com.example.dosediary.utils.DoseDiaryDatabase
import com.example.dosediary.model.entity.User
import com.example.dosediary.model.UserState
import com.example.dosediary.state.MedRefillState
import com.example.dosediary.state.MedicationWithNextRefillDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MedRefillViewModel  @Inject constructor(
    private val userState: UserState,
    application: Application
): ViewModel() {

    private val medicationDao = DoseDiaryDatabase.getInstance(application).medicationDao
    private val _currentUser: MutableStateFlow<User?> = userState.currentUser

    @OptIn(ExperimentalCoroutinesApi::class)
    val state: StateFlow<MedRefillState> = _currentUser.flatMapLatest { currentUser ->
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
                calculateNextRefillDate(
                    medication.startDate,
                    medication.endDate,
                    medication.refillDays,
                    medication.lastRefilledDate
                )?.let { refillDate ->
                    if (sdf.format(refillDate) == today) MedicationWithNextRefillDate(medication, refillDate) else null
                }
            }

            val medRefillsUpcoming = medications.mapNotNull { medication ->
                calculateNextRefillDate(
                    medication.startDate,
                    medication.endDate,
                    medication.refillDays,
                    medication.lastRefilledDate
                )?.let { refillDate ->
                    val refillDateFormatted = sdf.format(refillDate)
                    if (refillDateFormatted > today && refillDateFormatted <= nextWeekFormatted) MedicationWithNextRefillDate(medication, refillDate) else null
                }
            }

            MedRefillState(
                medRefillsToday = medRefillsToday,
                medRefillsUpcoming = medRefillsUpcoming
            )
//            MedRefillState(
//                medRefillsToday = medications.filter { medication ->
//                    calculateNextRefillDate(
//                        medication.startDate,
//                        medication.endDate,
//                        medication.refillDays,
//                        medication.lastRefilledDate
//                    )?.let { refillDate ->
//                        sdf.format(refillDate) == today
//                    } ?: false
//                },
//                medRefillsUpcoming = medications.filter { medication ->
//                    calculateNextRefillDate(
//                        medication.startDate,
//                        medication.endDate,
//                        medication.refillDays,
//                        medication.lastRefilledDate
//                    )?.let { refillDate ->
//                        val refillDateFormatted = sdf.format(refillDate)
//                        refillDateFormatted > today && refillDateFormatted <= nextWeekFormatted
//                    } ?: false
//                }
//            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), MedRefillState())

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
        }
    }

}

fun calculateNextRefillDate(startDate: Date, endDate: Date, refillDays: Int, lastRefilledDate: Date): Date? {
    val calendar = Calendar.getInstance()
    calendar.time = lastRefilledDate

    while (!calendar.time.after(endDate)) {
        calendar.add(Calendar.DAY_OF_YEAR, refillDays)
        if (calendar.time.after(lastRefilledDate)) {
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