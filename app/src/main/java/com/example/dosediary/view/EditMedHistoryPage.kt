@file:Suppress("UNUSED_EXPRESSION")

package com.example.dosediary.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.dosediary.R
import com.example.dosediary.components.CustomTopAppBar
import com.example.dosediary.components.DatePicker
import com.example.dosediary.components.TimePicker
import com.example.dosediary.event.EditMedHistoryEvent
import com.example.dosediary.model.entity.MedicationHistory
import com.example.dosediary.ui.theme.Primary
import com.example.dosediary.viewmodel.EditMedHistoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditMedicationPage(navController: NavHostController, viewModel: EditMedHistoryViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    val medicationName = remember { mutableStateOf("") }
    val effectivenessOptions = listOf(
        stringResource(id = R.string.effective),
        stringResource(id = R.string.moderate),
        stringResource(id = R.string.marginal),
        stringResource(id = R.string.ineffective)
    )
    val selectedEffectiveness = remember { mutableStateOf(state.effectiveness) }
    val calendar = Calendar.getInstance()
    val date = remember { mutableStateOf(calendar.time) }
    val time = remember { mutableStateOf(calendar.time) }
    val additionalDetails = remember { mutableStateOf(TextFieldValue(state.additionalDetails)) }
    val showConfirmDialog = remember { mutableStateOf(false) }

    // Formats (Talk to group if this needs to be changed)
    val dateFormat = SimpleDateFormat("MM-dd-yyyy", Locale.getDefault())
    val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

    val formattedDate = dateFormat.format(date.value)
    val formattedTime = timeFormat.format(time.value)

    if (showConfirmDialog.value) {
        EditMedicationConfirmationDialog(
            onDismiss = { showConfirmDialog.value = false },
            onConfirm = {
                val ownerId = 1
                val medicationHistory = MedicationHistory(
                    name = medicationName.value,
                    timeTaken = formattedTime,
                    dateTaken = formattedDate,
                    effectiveness = selectedEffectiveness.value,
                    ownerId = ownerId,
                    additionalDetails = additionalDetails.value.text
                )
                viewModel.onEvent(EditMedHistoryEvent.AddMedicationHistory(medicationHistory))
                showConfirmDialog.value = false
                navController.navigate("history")

                medicationName.value = ""
                selectedEffectiveness.value = ""
                date.value = calendar.time
                time.value = calendar.time
                additionalDetails.value = TextFieldValue("")
            }
        )
    }

    Scaffold(
        topBar = {
            CustomTopAppBar(
                header = stringResource(id = R.string.add_medication_history),
                showNavigationIcon = true,
                navController = navController,
                imageResId = R.drawable.icon,
                imageDescription = stringResource(id = R.string.app_icon)
            )
        }
    ) {innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            MedicationNameField(medicationName)
            Spacer(modifier = Modifier.height(16.dp))
            EffectivenessDropdown(
                selectedEffectiveness = selectedEffectiveness,
                effectivenessOptions = effectivenessOptions
            )
            Spacer(modifier = Modifier.height(16.dp))

            DatePicker(date.value, stringResource(id = R.string.select_date)) { selectedDate ->
                date.value = selectedDate
            }
            Spacer(modifier = Modifier.height(16.dp))
            TimePicker(time.value, stringResource(id = R.string.select_time)) { selectedTime ->
                time.value = selectedTime
            }
            Spacer(modifier = Modifier.height(16.dp))
            AdditionalDetailsField(additionalDetails) { details ->
                viewModel.onEvent(EditMedHistoryEvent.OnAdditionalDetailsChanged(details))
            }
            Spacer(modifier = Modifier.height(16.dp))
            ButtonRow(
                onSave = {
                    showConfirmDialog.value = true
                },
                onDiscard = {
                    navController.navigate("history")
                }
            )
        }
    }
}


@Composable
fun MedicationNameField(medicationName: MutableState<String>) {
    OutlinedTextField(
        value = medicationName.value,
        onValueChange = { medicationName.value = it },
        label = { Text(stringResource(id = R.string.medication_name)) },
        placeholder = { Text(stringResource(id = R.string.enter_medication_name)) },
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
            label = { Text(stringResource(id = R.string.effectiveness)) },
            placeholder = { Text(stringResource(id = R.string.select_effectiveness)) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
                .clickable { expanded = !expanded }
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
fun AdditionalDetailsField(
    additionalDetails: MutableState<TextFieldValue>,
    onAdditionalDetailsChanged: (String) -> Unit
) {
    OutlinedTextField(
        value = additionalDetails.value,
        onValueChange = {
            additionalDetails.value = it
            onAdditionalDetailsChanged(it.text)
        },
        label = { Text(stringResource(id = R.string.additional_details)) },
        placeholder = { Text(stringResource(id = R.string.enter_additional_details)) },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text)
    )
}

@Composable
fun ButtonRow(onSave: () -> Unit, onDiscard: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(
            onClick = onSave,
            colors = ButtonDefaults.buttonColors(containerColor = Primary),
            modifier = Modifier.weight(1f)
        ) {
            Text(stringResource(id = R.string.save))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Button(
            onClick = onDiscard,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF7676)),
            modifier = Modifier.weight(1f)
        ) {
            Text(stringResource(id = R.string.discard))
        }
    }
}

@Composable
fun EditMedicationConfirmationDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(id = R.string.confirm_save)) },
        text = { Text(stringResource(id = R.string.confirm_save_message)) },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(stringResource(id = R.string.yes))
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text(stringResource(id = R.string.no))
            }
        }
    )
}

@Preview(showBackground =true, name = "EditMedication Preview")
@Composable
fun EditMedPreview(){
    val navController = rememberNavController()
    EditMedicationPage(navController = navController);
}
