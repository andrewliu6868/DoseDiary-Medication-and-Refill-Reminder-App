package com.example.dosediary.state

import com.google.android.gms.maps.model.LatLng
import java.util.Date

data class AutocompleteResult(
    val address: String,
    val placeId: String,
)

data class UpsertMedicationState(
    val medicationId: Int = 0,
    val medicationName: String = "",
    val startDate: Date = Date(),
    val endDate: Date = Date(),
    val frequency: String = "1",
    val times: List<Date> = listOf(Date()),
    val refillDays: Int = 50,
    val note: String = "",
    val address: String = "",
    val postalCode: String = "",
    val postalCodeError: String? = null,
    val showDeleteConfirmDialog: Boolean = false,
    val showConfirmDialog: Boolean = false,
    val addressLatLng: LatLng = LatLng(0.0, 0.0),
    val locationAutofill: List<AutocompleteResult> = emptyList(),
)