package com.example.dosediary.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dosediary.state.AddMedicationState
import com.example.dosediary.events.AddMedicationEvent
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
class AddMedicationViewModel @Inject constructor(
    application: Application,
    private val userState: UserState
) : ViewModel() {
    private val medicationDao = DoseDiaryDatabase.getInstance(application).medicationDao
    private val _state = MutableStateFlow(AddMedicationState())
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
                postalCode = medication.postalCode
            )
        }
    }


    fun onEvent(event: AddMedicationEvent) {
        when (event) {
            is AddMedicationEvent.OnMedicationNameChanged -> {
                _state.value = _state.value.copy(medicationName = event.medicationName)
            }
            is AddMedicationEvent.OnStartDateChanged -> {
                _state.value = _state.value.copy(startDate = event.startDate)
            }
            is AddMedicationEvent.OnEndDateChanged -> {
                _state.value = _state.value.copy(endDate = event.endDate)
            }
            is AddMedicationEvent.OnFrequencyChanged -> {
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
            is AddMedicationEvent.OnTimeChanged -> {
                val times = _state.value.times.toMutableList()
                times[event.index] = event.time
                _state.value = _state.value.copy(times = times)
            }
            is AddMedicationEvent.OnRefillDaysChanged -> {
                _state.value = _state.value.copy(refillDays = event.refillDays)
            }
            is AddMedicationEvent.OnNoteChanged -> {
                _state.value = _state.value.copy(note = event.note)
            }
            is AddMedicationEvent.OnAddressChanged -> {
                _state.value = _state.value.copy(address = event.address)
                searchPlaces(event.address)
            }
            is AddMedicationEvent.OnPostalCodeChanged -> {
                val postalCodeRegex = Regex("^[A-Za-z]\\d[A-Za-z][ -]?\\d[A-Za-z]\\d$")
                val postalCodeError = if (postalCodeRegex.matches(event.postalCode)) null else "Invalid postal code format"
                _state.value = _state.value.copy(postalCode = event.postalCode, postalCodeError = postalCodeError)
            }
            is AddMedicationEvent.OnClickWithRipple -> {
                getCoordinates(event.autoCompleteResult)
                _state.value = _state.value.copy(
                    address = event.autoCompleteResult.address,
                    locationAutofill = emptyList()
                )
            }
            AddMedicationEvent.SaveMedication -> {
                _state.value = _state.value.copy(showConfirmDialog = true)
            }
            AddMedicationEvent.ConfirmSaveMedication -> {
                viewModelScope.launch {
                    val medication = _state.value.toMedication()
                    medicationDao.upsertMedication(medication)
                    _state.value = AddMedicationState()
                }
            }
            AddMedicationEvent.DismissSaveDialog -> {
                _state.value = _state.value.copy(showConfirmDialog = false)
            }
            AddMedicationEvent.DeleteMedication -> {
                _state.value = _state.value.copy(showDeleteConfirmDialog = true)
            }
            AddMedicationEvent.ConfirmDeleteMedication -> {
                viewModelScope.launch {
                    medicationDao.deleteMedicationById(_state.value.medicationId)
                    _state.value = AddMedicationState()
                }
            }
            AddMedicationEvent.DismissDeleteDialog -> {
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

    private fun AddMedicationState.toMedication(): Medication {
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
            postalCode = this.postalCode,
            postalCodeError = this.postalCodeError,
            lastRefilledDate = this.startDate,
            owner = 1  // TODO: Change this after User Profile is Ready
        )
    }

}
