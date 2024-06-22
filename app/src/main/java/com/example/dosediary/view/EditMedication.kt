package com.example.dosediary.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditMedication() {
    val medicationName = remember { mutableStateOf("Ibuprofen") }
    val effectivenessOptions = listOf("Effective", "Moderate", "Marginal", "Ineffective")
    val selectedEffectiveness = remember { mutableStateOf(effectivenessOptions[0]) }
    val calendar = Calendar.getInstance()
    calendar.set(2023, Calendar.MAY, 10, 10, 30)
    val date = remember { mutableStateOf(calendar.time) }
    val time = remember { mutableStateOf(calendar.time) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        HeaderText()
        MedicationNameField(medicationName)
        Spacer(modifier = Modifier.height(16.dp))
        EffectivenessDropdown(selectedEffectiveness, effectivenessOptions)
        Spacer(modifier = Modifier.height(16.dp))
        DateField(date)
        Spacer(modifier = Modifier.height(16.dp))
        TimeField(time)
    }
}

@Composable
fun HeaderText() {
    Text(
        text = "Edit Medication History",
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(bottom = 16.dp)
    )
}

@Composable
fun MedicationNameField(medicationName: MutableState<String>) {
    OutlinedTextField(
        value = medicationName.value,
        onValueChange = { medicationName.value = it },
        label = { Text("Medication Name") },
        modifier = Modifier.fillMaxWidth()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EffectivenessDropdown(
    selectedEffectiveness: MutableState<String>,
    effectivenessOptions: List<String>
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedEffectiveness.value,
            onValueChange = { },
            readOnly = true,
            label = { Text("Effectiveness") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier.fillMaxWidth().menuAnchor().clickable { expanded = !expanded }
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            effectivenessOptions.forEach { effectiveness ->
                DropdownMenuItem(
                    text = { Text(effectiveness) },
                    onClick = {
                        selectedEffectiveness.value = effectiveness
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun DateField(date: MutableState<Date>) {
    val dateFormat = remember { SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()) }
    var datePickerVisible by remember { mutableStateOf(false) }

    if (datePickerVisible) {
        DatePicker(
            date = date.value
        )
    }

    OutlinedTextField(
        value = dateFormat.format(date.value),
        onValueChange = { },
        label = { Text("Select Date") },
        readOnly = true,
        modifier = Modifier.fillMaxWidth().clickable { datePickerVisible = true },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
    )
}

@Composable
fun TimeField(time: MutableState<Date>) {
    val timeFormat = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }
    var timePickerVisible by remember { mutableStateOf(false) }

    if (timePickerVisible) {
        TimePicker(
            time = time.value
        )
    }

    OutlinedTextField(
        value = timeFormat.format(time.value),
        onValueChange = { },
        label = { Text("Select Time") },
        readOnly = true,
        modifier = Modifier.fillMaxWidth().clickable { timePickerVisible = true },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
    )
}

@Composable
fun DatePicker(
    date: Date
) {

    val calendar = Calendar.getInstance()
    calendar.time = date
}

@Composable
fun TimePicker(
    time: Date
) {
    val calendar = Calendar.getInstance()
    calendar.time = time
}
