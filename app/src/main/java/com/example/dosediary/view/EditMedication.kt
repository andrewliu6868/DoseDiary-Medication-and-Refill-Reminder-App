@file:Suppress("UNUSED_EXPRESSION")

package com.example.dosediary.view

import android.widget.DatePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.dosediary.ui.theme.Primary
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
        Spacer(modifier = Modifier.height(16.dp))
        ButtonRow()
    }
}

@Composable
fun HeaderText() {
    Text(
        text = "Edit Medication History",
        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
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
    val calendar = Calendar.getInstance().apply { time = date.value }

    val context = LocalContext.current

    val datePickerDialog = remember {
        android.app.DatePickerDialog(
            context,
            { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                calendar.set(year, month, dayOfMonth)
                date.value = calendar.time
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }


    OutlinedTextField(
        value = dateFormat.format(date.value),
        onValueChange = { },
        label = { Text("Select Date") },
        readOnly = true,
        modifier = Modifier.fillMaxWidth(),
        interactionSource = remember { MutableInteractionSource() }
            .also { interactionSource ->
                LaunchedEffect(interactionSource) {
                    interactionSource.interactions.collect {
                        if (it is PressInteraction.Release) {
                            datePickerDialog.show()
                        }
                    }
                }
            }
    )

}


@Composable
fun TimeField(time: MutableState<Date>) {
    val timeFormat = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }
    var timePickerVisible by remember { mutableStateOf(true) }

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
fun ButtonRow() {
    Row(
        modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(
            onClick = {null},
            colors = ButtonDefaults.buttonColors(containerColor = Primary),
            modifier = Modifier.weight(1f)
        ) {
            Text("Save")
        }
        Spacer(modifier = Modifier.width(16.dp))
        Button(
            onClick = {null},
            colors = ButtonDefaults.buttonColors(containerColor = Primary),
            modifier = Modifier.weight(1f)
        ) {
            Text("Delete")
        }
    }
}

@Composable
fun TimePicker(
    time: Date
) {
    val calendar = Calendar.getInstance()
    calendar.time = time
}
