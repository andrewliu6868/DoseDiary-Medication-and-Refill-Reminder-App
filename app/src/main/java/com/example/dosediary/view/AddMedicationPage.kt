package com.example.dosediary.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.dosediary.R
import com.example.dosediary.components.CustomTopAppBar
import com.example.dosediary.components.DatePicker
import com.example.dosediary.components.TimePicker
import com.example.dosediary.events.AddMedicationEvent
import com.example.dosediary.state.AddMedicationState
import com.example.dosediary.ui.theme.Primary
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AddMedicationPage(
    navController: NavHostController,
    state: AddMedicationState,
    onEvent: (AddMedicationEvent) -> Unit
) {

    Scaffold(
        topBar = {
            CustomTopAppBar(
                header = stringResource(R.string.add_medication),
                showNavigationIcon = true,
                navController = navController,
                imageResId = R.drawable.icon,  // Customizable icon
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
            item { AddressSection(state.address, state.postalCode, state.postalCodeError) { onEvent(it) } }
            item { NoteSection(state.note) { onEvent(it) } }
            item { SaveDeleteRow(navController, state.medicationId) { onEvent(it) } }
        }
        if (state.showConfirmDialog) {
            SaveConfirmationDialog(
                onConfirm = {
                    onEvent(AddMedicationEvent.ConfirmSaveMedication)
                    navController.navigateUp()
                },
                onDismiss = { onEvent(AddMedicationEvent.DismissSaveDialog) }
            )
        }
        if (state.showDeleteConfirmDialog) {
            DeleteConfirmationDialog(
                onConfirm = {
                    onEvent(AddMedicationEvent.ConfirmDeleteMedication)
                    navController.navigateUp()
                },
                onDismiss = { onEvent(AddMedicationEvent.DismissDeleteDialog) }
            )
        }
    }
}

@Composable
fun MedicationNameSection(medicationName: String, onEvent: (AddMedicationEvent) -> Unit) {
    Text(
        text = stringResource(R.string.medication_name),
        style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, fontSize = 17.sp)
    )
    Spacer(modifier = Modifier.height(5.dp))
    OutlinedTextField(
        value = medicationName,
        onValueChange = { onEvent(AddMedicationEvent.OnMedicationNameChanged(it)) },
        label = { Text(stringResource(R.string.name)) },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun MedDurationSection(startDate: Date, endDate: Date, onEvent: (AddMedicationEvent) -> Unit) {
    Text(
        text = stringResource(R.string.medication_period),
        style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, fontSize = 17.sp)
    )
    Spacer(modifier = Modifier.height(5.dp))
    DatePicker(date = startDate, placeholder = stringResource(R.string.start_date)) {
        onEvent(AddMedicationEvent.OnStartDateChanged(it))
    }
    Spacer(modifier = Modifier.height(5.dp))
    DatePicker(date = endDate, placeholder = stringResource(R.string.end_date)) {
        onEvent(AddMedicationEvent.OnEndDateChanged(it))
    }
}

@Composable
fun MedFrequencySection(frequency: String, times: List<Date>, onEvent: (AddMedicationEvent) -> Unit) {
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
                onEvent(AddMedicationEvent.OnFrequencyChanged(it.text))
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }
    Spacer(modifier = Modifier.height(10.dp))
    times.forEachIndexed { index, time ->
        TimePicker(time = time, placeholder = stringResource(R.string.select_time, index + 1)) {
            onEvent(AddMedicationEvent.OnTimeChanged(index, it))
        }
        Spacer(modifier = Modifier.height(10.dp))
    }
    Spacer(modifier = Modifier.height(5.dp))

    // Testing Purpose
    val dateFormat = remember { SimpleDateFormat("MM/dd/yyyy hh:mm a", Locale.getDefault()) }
    times.forEachIndexed { index, time ->
        Text(text = stringResource(R.string.time, index + 1, dateFormat.format(time)))
    }
}

@Composable
fun RefillDaysSection(sliderPosition: Int, onEvent: (AddMedicationEvent) -> Unit) {
    Text(
        text = stringResource(R.string.refill_days),
        style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, fontSize = 17.sp)
    )
    Spacer(modifier = Modifier.height(8.dp))
    Slider(
        value = sliderPosition.toFloat(),
        onValueChange = { onEvent(AddMedicationEvent.OnRefillDaysChanged(it.toInt())) },
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
fun NoteSection(note: String, onEvent: (AddMedicationEvent) -> Unit) {
    Text(
        text = stringResource(R.string.notes),
        style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, fontSize = 17.sp)
    )
    Spacer(modifier = Modifier.height(5.dp))

    OutlinedTextField(
        value = note,
        onValueChange = { onEvent(AddMedicationEvent.OnNoteChanged(it)) },
        label = { Text(stringResource(R.string.add_note)) },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun AddressSection(address: String, postalCode: String, postalCodeError: String?, onEvent: (AddMedicationEvent) -> Unit) {
    Text(
        text = stringResource(R.string.pharmacy_location),
        style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, fontSize = 17.sp)
    )
    Spacer(modifier = Modifier.height(10.dp))

    // Address
    OutlinedTextField(
        value = address,
        onValueChange = { onEvent(AddMedicationEvent.OnAddressChanged(it)) },
        label = { Text(stringResource(R.string.address)) },
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(10.dp))

    // Postal code
    OutlinedTextField(
        value = postalCode,
        onValueChange = { onEvent(AddMedicationEvent.OnPostalCodeChanged(it)) },
        label = { Text(stringResource(R.string.postal_code)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        modifier = Modifier.fillMaxWidth(),
        isError = postalCodeError != null
    )

    if (postalCodeError != null) {
        Text(
            text = postalCodeError,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

@Composable
fun SaveDeleteRow(navController: NavHostController, medicationId: Int, onEvent: (AddMedicationEvent) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(
            onClick = { onEvent(AddMedicationEvent.SaveMedication) },
            colors = ButtonDefaults.buttonColors(containerColor = Primary),
            modifier = Modifier.weight(1f),
            elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 5.dp)
        ) {
            Text(stringResource(R.string.save))
        }

        if (medicationId != 0) {
            Spacer(modifier = Modifier.width(10.dp))
            Button(
                onClick = { onEvent(AddMedicationEvent.DeleteMedication) },
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
    val state = AddMedicationState()
    val onEvent: (AddMedicationEvent) -> Unit = {}
    AddMedicationPage(navController = navController, state = state, onEvent = onEvent)
}
