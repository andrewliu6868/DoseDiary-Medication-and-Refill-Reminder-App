package com.example.dosediary.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dosediary.state.UpsertMedicationState
import com.example.dosediary.events.UpsertMedicationEvent
import com.example.dosediary.state.UserState
import com.example.dosediary.model.entity.Medication
import com.example.dosediary.state.AutocompleteResult
import com.example.dosediary.utils.DoseDiaryDatabase
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class UpsertMedicationViewModel @Inject constructor(
    application: Application,
    private val userState: UserState
) : ViewModel() {
    private val medicationDao = DoseDiaryDatabase.getInstance(application).medicationDao
    private val _state = MutableStateFlow(UpsertMedicationState())
    val state = _state.asStateFlow()

    lateinit var placesClient: PlacesClient
    lateinit var fusedLocationClient: FusedLocationProviderClient
    private var job: Job? = null

    fun initialize(medication: Medication?) {
        if (medication != null) {
            _state.value = _state.value.copy(
                medicationId = medication.id,
                medicationName = medication.medicationName,
                startDate = medication.startDate,
                endDate = medication.endDate,
                frequency = medication.frequency,
                times = medication.times,
                refillDays = medication.refillDays,
                note = medication.note,
                address = medication.address,
            )
        } else {
            _state.value = UpsertMedicationState() // Reset state for adding new medication
        }
    }


    fun onEvent(event: UpsertMedicationEvent) {
        when (event) {
            is UpsertMedicationEvent.OnMedicationNameChanged -> {
                _state.value = _state.value.copy(medicationName = event.medicationName)
            }
            is UpsertMedicationEvent.OnStartDateChanged -> {
                _state.value = _state.value.copy(startDate = event.startDate)
            }
            is UpsertMedicationEvent.OnEndDateChanged -> {
                _state.value = _state.value.copy(endDate = event.endDate)
            }
            is UpsertMedicationEvent.OnFrequencyChanged -> {
                val frequency = event.frequency
                val intVal = frequency.toIntOrNull()
                if (intVal != null && intVal > 0) {
                    val times = _state.value.times.toMutableList()
                    while (times.size < intVal) {
                        times.add(Date())
                    }
                    while (times.size > intVal) {
                        times.removeAt(times.size - 1)
                    }
                    _state.value = _state.value.copy(frequency = frequency, times = times)
                } else {
                    _state.value = _state.value.copy(frequency = frequency)
                }
            }
            is UpsertMedicationEvent.OnTimeChanged -> {
                val times = _state.value.times.toMutableList()
                times[event.index] = event.time
                _state.value = _state.value.copy(times = times)
            }
            is UpsertMedicationEvent.OnRefillDaysChanged -> {
                _state.value = _state.value.copy(refillDays = event.refillDays)
            }
            is UpsertMedicationEvent.OnNoteChanged -> {
                _state.value = _state.value.copy(note = event.note)
            }
            is UpsertMedicationEvent.OnAddressChanged -> {
                _state.value = _state.value.copy(address = event.address)
                searchPlaces(event.address)
            }
            is UpsertMedicationEvent.OnClickWithRipple -> {
                getCoordinates(event.autoCompleteResult)
                _state.value = _state.value.copy(
                    address = event.autoCompleteResult.address,
                    locationAutofill = emptyList()
                )
            }
            UpsertMedicationEvent.SaveMedication -> {
                _state.value = _state.value.copy(showConfirmDialog = true)
            }
            UpsertMedicationEvent.ConfirmSaveMedication -> {
                viewModelScope.launch {
                    val medication = _state.value.toMedication()
                    medicationDao.upsertMedication(medication)
                    _state.value = UpsertMedicationState()
                }
            }
            UpsertMedicationEvent.DismissSaveDialog -> {
                _state.value = _state.value.copy(showConfirmDialog = false)
            }
            UpsertMedicationEvent.DeleteMedication -> {
                _state.value = _state.value.copy(showDeleteConfirmDialog = true)
            }
            UpsertMedicationEvent.ConfirmDeleteMedication -> {
                viewModelScope.launch {
                    medicationDao.deleteMedicationById(_state.value.medicationId)
                    _state.value = UpsertMedicationState()
                }
            }
            UpsertMedicationEvent.DismissDeleteDialog -> {
                _state.value = _state.value.copy(showDeleteConfirmDialog = false)
            }
        }
    }

    fun searchPlaces(query: String) {
        job?.cancel()
        _state.value = _state.value.copy (locationAutofill = emptyList())
        job = viewModelScope.launch {
            val request = FindAutocompletePredictionsRequest.builder().setQuery(query).build()
            placesClient.findAutocompletePredictions(request).addOnSuccessListener { response ->
                _state.value = _state.value.copy(locationAutofill = _state.value.locationAutofill + response.autocompletePredictions.map {
                    AutocompleteResult(
                        it.getFullText(null).toString(), it.placeId
                    )
                })
//                locationAutofill += response.autocompletePredictions.map {
//                    AutocompleteResult(
//                        it.getFullText(null).toString(), it.placeId
//                    )
//                }
            }.addOnFailureListener {
                it.printStackTrace()
                println(it.cause)
                println(it.message)
            }
        }
    }

    fun getCoordinates(result: AutocompleteResult) {
        val placeFields = listOf(Place.Field.LAT_LNG)
        val request = FetchPlaceRequest.newInstance(result.placeId, placeFields)
        placesClient.fetchPlace(request).addOnSuccessListener {
            if (it != null) {
                _state.value = _state.value.copy(
                    addressLatLng = it.place.latLng!!
                )
            }
        }.addOnFailureListener {
            it.printStackTrace()
        }
    }

        private fun UpsertMedicationState.toMedication(): Medication {
        return Medication(
            id = this.medicationId,
            medicationName = this.medicationName,
            startDate = this.startDate,
            endDate = this.endDate,
            frequency = this.frequency,
            times = this.times,
            refillDays = this.refillDays,
            note = this.note,
            address = this.address,
            addressLatLng = this.addressLatLng,
            lastRefilledDate = this.startDate,
            owner = 1  // TODO: Change this after User Profile is Ready
        )
    }
}