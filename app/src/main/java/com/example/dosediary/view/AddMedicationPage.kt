package com.example.dosediary.view


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
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
import java.util.Date
import java.util.Locale

@Composable
fun AddMedicationPage(
    navController: NavHostController,
    state: AddMedicationState,
    onEvent: (AddMedicationEvent) -> Unit
) {

    Scaffold(
        topBar = {
            CustomTopAppBar(
                header = "Add Medication",
                showNavigationIcon = true,
                navController = navController,
                imageResId = R.drawable.icon,  // Customizable icon
                imageDescription = "App Icon"
            )
        }
    ){ innerPadding ->
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
                    navController.navigateUp() },
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
    Text(text = "Medication Name", style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, fontSize = 17.sp))
    Spacer(modifier = Modifier.height(5.dp))
    OutlinedTextField(
        value = medicationName,
        onValueChange = { onEvent(AddMedicationEvent.OnMedicationNameChanged(it)) },
        label = { Text("Name") },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun MedDurationSection(startDate: Date, endDate: Date, onEvent: (AddMedicationEvent) -> Unit) {
    Text(text = "Medication Period", style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, fontSize = 17.sp))
    Spacer(modifier = Modifier.height(5.dp))
    DatePicker(date = startDate, placeholder = "Start Date") {
        onEvent(AddMedicationEvent.OnStartDateChanged(it))
    }
    Spacer(modifier = Modifier.height(5.dp))
    DatePicker(date = endDate, placeholder = "End Date") {
        onEvent(AddMedicationEvent.OnEndDateChanged(it))
    }}

@Composable
fun MedFrequencySection(frequency: String, times: List<Date>, onEvent: (AddMedicationEvent) -> Unit) {
    var frequencyState by remember { mutableStateOf(TextFieldValue(frequency)) }

    Text(text = "Intake Frequency", style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, fontSize = 17.sp))
    Spacer(modifier = Modifier.height(5.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Column {
            Spacer(modifier = Modifier.height(15.dp))
            Text(text = "Number of times per day:", style = LocalTextStyle.current.copy(fontWeight = FontWeight.Medium, fontSize = 15.sp))
        }
        Spacer(modifier = Modifier.width(8.dp))
        OutlinedTextField(
            modifier = Modifier
                .width(80.dp)
                .onFocusChanged { focusState ->
                    if (focusState.isFocused) {
                        frequencyState = frequencyState.copy(selection = TextRange(0, frequencyState.text.length))
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
        TimePicker(time = time, placeholder = "Select Time ${index + 1}") {
            onEvent(AddMedicationEvent.OnTimeChanged(index, it))
        }
        Spacer(modifier = Modifier.height(10.dp))
    }
    Spacer(modifier = Modifier.height(5.dp))

    // Testing Purpose
    val dateFormat = remember { SimpleDateFormat("MM/dd/yyyy hh:mm a", Locale.getDefault()) }
    times.forEachIndexed { index, time ->
        Text(text = "Time ${index + 1}: ${dateFormat.format(time)}")
    }
}


@Composable
fun RefillDaysSection(sliderPosition: Int, onEvent: (AddMedicationEvent) -> Unit) {
    Text(text = "Refill Days", style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, fontSize = 17.sp))
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
    Text(text = "Notes", style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, fontSize = 17.sp))
    Spacer(modifier = Modifier.height(5.dp))

    OutlinedTextField(
        value = note,
        onValueChange = { onEvent(AddMedicationEvent.OnNoteChanged(it)) },
        label = { Text("Add a note") },
        modifier = Modifier.fillMaxWidth()
    )
}


@Composable
fun AddressSection(address: String, postalCode: String, postalCodeError: String?, onEvent: (AddMedicationEvent) -> Unit) {
    Text(text = "Pharmacy Location", style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, fontSize = 17.sp))
    Spacer(modifier = Modifier.height(10.dp))

    // Address
    OutlinedTextField(
        value = address,
        onValueChange = { onEvent(AddMedicationEvent.OnAddressChanged(it)) },
        label = { Text("Address") },
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(10.dp))

    // Postal code
    OutlinedTextField(
        value = postalCode,
        onValueChange = { onEvent(AddMedicationEvent.OnPostalCodeChanged(it)) },
        label = { Text("Postal Code") },
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
        //TODO: Add Validation For Save (All fields besides Notes should be filled)
        Button(
            onClick = { onEvent(AddMedicationEvent.SaveMedication) },
            colors = ButtonDefaults.buttonColors(containerColor = Primary),
            modifier = Modifier.weight(1f),
            elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 5.dp)
        ) {
            Text("Save")
        }

        if (medicationId != 0) {
            Spacer(modifier = Modifier.width(10.dp))
            Button(
                onClick = { onEvent(AddMedicationEvent.DeleteMedication) },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                modifier = Modifier.weight(1f),
                elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 5.dp)
            ) {
                Text("Delete")
            }
        }
    }
}


@Composable
fun SaveConfirmationDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Confirm Save") },
        text = { Text(text = "Are you sure the information is correct?") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Yes")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("No")
            }
        }
    )
}

@Composable
fun DeleteConfirmationDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Confirm Delete") },
        text = { Text(text = "Are you sure you want to delete this medication?") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Yes")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("No")
            }
        }
    )
}

@Preview(showBackground =true, name = "AddMedication Preview")
@Composable
fun AddMedPreview() {
    val navController = rememberNavController()
    val state = AddMedicationState()
    val onEvent: (AddMedicationEvent) -> Unit = {}
    AddMedicationPage(navController = navController, state = state, onEvent = onEvent)
}

