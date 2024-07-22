package com.example.dosediary.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.dosediary.R
import com.example.dosediary.components.CustomTopAppBar
import com.example.dosediary.components.DatePicker
import com.example.dosediary.components.TimePicker
import com.example.dosediary.events.AddMedicationEvent
import com.example.dosediary.state.AutocompleteResult
import com.example.dosediary.events.UpsertMedicationEvent
import com.example.dosediary.state.UpsertMedicationState
import com.example.dosediary.viewmodel.ReminderViewModel
import com.example.dosediary.ui.theme.Primary
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun UpsertMedicationPage(
    navController: NavHostController,
    state: UpsertMedicationState,
    onEvent: (UpsertMedicationEvent) -> Unit
) {
    val navBackStackEntry = navController.currentBackStackEntry
    val mode = navBackStackEntry?.arguments?.getString("mode") ?: "edit"

    Scaffold(
        topBar = {
            CustomTopAppBar(
                header = if (mode == "add") stringResource(R.string.add_medication) else stringResource(R.string.edit_medication),
                showNavigationIcon = true,
                navController = navController,
                imageResId = R.drawable.icon,
                imageDescription = stringResource(R.string.app_icon)
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding)
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item { MedicationNameSection(state.medicationName) { onEvent(it) } }
            item { MedDurationSection(state.startDate, state.endDate) { onEvent(it) } }
            item { MedFrequencySection(state.frequency, state.times) { onEvent(it) } }
            item { RefillDaysSection(state.refillDays) { onEvent(it) } }
            item { AddressSection(state.address, state.locationAutofill) { onEvent(it) } }
            item { NoteSection(state.note) { onEvent(it) } }
            if (mode == "edit") {
                item { SaveDeleteRow(navController, state.medicationId) { onEvent(it) } }
            } else {
                item { SaveRow(navController, onEvent, state.medicationName, state.times, state.startDate, state.endDate, state.refillDays) }
            }
        }
        if (state.showConfirmDialog) {
            SaveConfirmationDialog(
                onConfirm = {
                    onEvent(UpsertMedicationEvent.ConfirmSaveMedication)
                    navController.navigateUp()
                },
                onDismiss = { onEvent(UpsertMedicationEvent.DismissSaveDialog) }
            )
        }
        if (state.showDeleteConfirmDialog) {
            DeleteConfirmationDialog(
                onConfirm = {
                    onEvent(UpsertMedicationEvent.ConfirmDeleteMedication)
                    navController.navigateUp()
                },
                onDismiss = { onEvent(UpsertMedicationEvent.DismissDeleteDialog) }
            )
        }
    }
}

@Composable
fun MedicationNameSection(medicationName: String, onEvent: (UpsertMedicationEvent) -> Unit) {
    Text(
        text = stringResource(R.string.medication_name),
        style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, fontSize = 17.sp)
    )
    Spacer(modifier = Modifier.height(5.dp))
    OutlinedTextField(
        value = medicationName,
        onValueChange = { onEvent(UpsertMedicationEvent.OnMedicationNameChanged(it)) },
        label = { Text(stringResource(R.string.name)) },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun MedDurationSection(startDate: Date, endDate: Date, onEvent: (UpsertMedicationEvent) -> Unit) {
    Text(
        text = stringResource(R.string.medication_period),
        style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, fontSize = 17.sp)
    )
    Spacer(modifier = Modifier.height(5.dp))
    DatePicker(date = startDate, placeholder = stringResource(R.string.start_date)) {
        onEvent(UpsertMedicationEvent.OnStartDateChanged(it))
    }
    Spacer(modifier = Modifier.height(5.dp))
    DatePicker(date = endDate, placeholder = stringResource(R.string.end_date)) {
        onEvent(UpsertMedicationEvent.OnEndDateChanged(it))
    }
}

@Composable
fun MedFrequencySection(frequency: String, times: List<Date>, onEvent: (UpsertMedicationEvent) -> Unit) {
    var frequencyState by remember { mutableStateOf(TextFieldValue(frequency)) }

    Text(
        text = stringResource(R.string.intake_frequency),
        style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, fontSize = 17.sp)
    )
    Spacer(modifier = Modifier.height(5.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Column {
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = stringResource(R.string.times_per_day),
                style = LocalTextStyle.current.copy(fontWeight = FontWeight.Medium, fontSize = 15.sp)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        OutlinedTextField(
            modifier = Modifier
                .width(80.dp)
                .onFocusChanged { focusState ->
                    if (focusState.isFocused) {
                        frequencyState = frequencyState.copy(
                            selection = TextRange(
                                0,
                                frequencyState.text.length
                            )
                        )
                    }
                },
            value = frequencyState,
            onValueChange = {
                frequencyState = it
                onEvent(UpsertMedicationEvent.OnFrequencyChanged(it.text))
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }
    Spacer(modifier = Modifier.height(10.dp))
    times.forEachIndexed { index, time ->
        TimePicker(time = time, placeholder = stringResource(R.string.select_time, index + 1)) {
            onEvent(UpsertMedicationEvent.OnTimeChanged(index, it))
        }
        Spacer(modifier = Modifier.height(10.dp))
    }
    Spacer(modifier = Modifier.height(5.dp))

    // Testing Purpose
//    val dateFormat = remember { SimpleDateFormat("MM/dd/yyyy hh:mm a", Locale.getDefault()) }
//    times.forEachIndexed { index, time ->
//        Text(text = stringResource(R.string.time, index + 1, dateFormat.format(time)))
//    }
}

@Composable
fun RefillDaysSection(sliderPosition: Int, onEvent: (UpsertMedicationEvent) -> Unit) {
    Text(
        text = stringResource(R.string.refill_days),
        style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, fontSize = 17.sp)
    )
    Spacer(modifier = Modifier.height(8.dp))
    Slider(
        value = sliderPosition.toFloat(),
        onValueChange = { onEvent(UpsertMedicationEvent.OnRefillDaysChanged(it.toInt())) },
        valueRange = 0f..100f,
        steps = 100,
        colors = SliderDefaults.colors(
            thumbColor = Primary,               // Color of the thumb (the draggable circle)
            activeTrackColor = Primary,         // Color of the active track (left of the thumb)
        )
    )
    Text(modifier = Modifier.fillMaxWidth(), text = sliderPosition.toString(), textAlign = TextAlign.Center)
}

@Composable
fun NoteSection(note: String, onEvent: (UpsertMedicationEvent) -> Unit) {
    Text(
        text = stringResource(R.string.notes),
        style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, fontSize = 17.sp)
    )
    Spacer(modifier = Modifier.height(5.dp))

    OutlinedTextField(
        value = note,
        onValueChange = { onEvent(UpsertMedicationEvent.OnNoteChanged(it)) },
        label = { Text(stringResource(R.string.add_note)) },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun AddressSection(address: String, locationAutofill:List<AutocompleteResult>, onEvent: (UpsertMedicationEvent) -> Unit) {
    Text(
        text = stringResource(R.string.pharmacy_location),
        style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, fontSize = 17.sp)
    )
    Spacer(modifier = Modifier.height(10.dp))

    AnimatedVisibility(
        locationAutofill.isNotEmpty(),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .height(150.dp)
        ) {
            items(locationAutofill) {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clickable {
                        onEvent(UpsertMedicationEvent.OnClickWithRipple(it))
                    }

                ) {
                    Text(it.address)
                }
            }
        }
        Spacer(Modifier.height(16.dp))
    }
    // Address
    OutlinedTextField(
        value = address,
        onValueChange = { onEvent(UpsertMedicationEvent.OnAddressChanged(it)) },
        label = { Text(stringResource(R.string.address)) },
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(10.dp))

    // Postal code
//    OutlinedTextField(
//        value = postalCode,
//        onValueChange = { onEvent(UpsertMedicationEvent.OnPostalCodeChanged(it)) },
//        label = { Text(stringResource(R.string.postal_code)) },
//        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
//        modifier = Modifier.fillMaxWidth(),
//        isError = postalCodeError != null
//    )
//
//    if (postalCodeError != null) {
//        Text(
//            text = postalCodeError,
//            color = MaterialTheme.colorScheme.error,
//            style = MaterialTheme.typography.bodySmall,
//            modifier = Modifier.padding(start = 16.dp)
//        )
//    }
}

@Composable
fun SaveDeleteRow(navController: NavHostController, medicationId: Int, onEvent: (UpsertMedicationEvent) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(
            onClick = { onEvent(UpsertMedicationEvent.SaveMedication) },
            colors = ButtonDefaults.buttonColors(containerColor = Primary),
            modifier = Modifier.weight(1f),
            elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 5.dp)
        ) {
            Text(stringResource(R.string.save))
        }

        if (medicationId != 0) {
            Spacer(modifier = Modifier.width(10.dp))
            Button(
                onClick = { onEvent(UpsertMedicationEvent.DeleteMedication) },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                modifier = Modifier.weight(1f),
                elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 5.dp)
            ) {
                Text(stringResource(R.string.delete))
            }
        }
    }
}

@Composable
fun SaveRow(navController: NavHostController, onEvent: (UpsertMedicationEvent) -> Unit, medName: String, times: List<Date>, startDate: Date, endDate: Date, refillDays: Int) {
    val reminderViewModel = hiltViewModel<ReminderViewModel>()
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(
            onClick = { onEvent(UpsertMedicationEvent.SaveMedication)
                reminderViewModel.scheduleMedReminders(medName, times, startDate,endDate)
                reminderViewModel.scheduleRefill(medName, refillDays)
             },
            colors = ButtonDefaults.buttonColors(containerColor = Primary),
            modifier = Modifier.weight(1f),
            elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 5.dp)
        ) {
            Text(stringResource(R.string.save))
        }
    }
}

@Composable
fun SaveConfirmationDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(R.string.confirm_save)) },
        text = { Text(text = stringResource(R.string.confirm_save_message)) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(stringResource(R.string.yes))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.no))
            }
        }
    )
}

@Composable
fun DeleteConfirmationDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(R.string.confirm_delete)) },
        text = { Text(text = stringResource(R.string.confirm_delete_message)) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(stringResource(R.string.yes))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.no))
            }
        }
    )
}

@Preview(showBackground = true, name = "AddMedication Preview")
@Composable
fun AddMedPreview() {
    val navController = rememberNavController()
    val state = UpsertMedicationState()
    val onEvent: (UpsertMedicationEvent) -> Unit = {}
    UpsertMedicationPage(navController = navController, state = state, onEvent = onEvent)
}