@file:Suppress("UNUSED_EXPRESSION")

package com.example.dosediary.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.dosediary.R
import com.example.dosediary.components.CustomTopAppBar
import com.example.dosediary.components.DatePicker
import com.example.dosediary.components.TimePicker
import com.example.dosediary.event.UpsertMedHistoryEvent
import com.example.dosediary.state.UpsertMedHistoryState
import com.example.dosediary.ui.theme.Primary
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpsertMedicationHistoryPage(
    navController: NavHostController,
    state: UpsertMedHistoryState,
    onEvent: (UpsertMedHistoryEvent) -> Unit
) {
    val mode = navController.currentBackStackEntry?.arguments?.getString("mode") ?: "edit"

    if (state.showConfirmDialog) {
        EditMedicationConfirmationDialog(
            onDismiss = { onEvent(UpsertMedHistoryEvent.OnConfirmDialogDismissed) },
            onConfirm = {
                onEvent(UpsertMedHistoryEvent.OnConfirmClicked)
                navController.navigate("history")
            }
        )
    }

    Scaffold(
        topBar = {
            CustomTopAppBar(
                header = if (mode == "add") stringResource(R.string.add_medication) else stringResource(R.string.edit_medication),
                showNavigationIcon = true,
                navController = navController,
                imageResId = R.drawable.icon,
                imageDescription = stringResource(id = R.string.app_icon)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            MedicationNameField(
                value = state.name,
                onValueChange = { onEvent(UpsertMedHistoryEvent.OnMedicationNameChanged(it)) },
                mode = mode
            )
            Spacer(modifier = Modifier.height(16.dp))
            EffectivenessDropdown(
                selectedEffectiveness = state.effectiveness,
                effectivenessOptions = listOf(
                    stringResource(id = R.string.effective),
                    stringResource(id = R.string.moderate),
                    stringResource(id = R.string.marginal),
                    stringResource(id = R.string.ineffective)
                ),
                onEffectivenessChange = { onEvent(UpsertMedHistoryEvent.OnEffectivenessChanged(it)) }
            )
            Spacer(modifier = Modifier.height(16.dp))
            if (mode != "edit") {
                DatePicker(
                    date = parseDate(state.dateTaken),
                    placeholder = stringResource(id = R.string.select_date),
                    onDateSelected = { onEvent(UpsertMedHistoryEvent.OnDateChanged(it.toString())) }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            if (mode != "edit") {
                TimePicker(
                    time = parseTime(state.timeTaken),
                    placeholder = stringResource(id = R.string.select_time),
                    onTimeSelected = { onEvent(UpsertMedHistoryEvent.OnTimeChanged(it.toString())) }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            AdditionalDetailsField(
                value = TextFieldValue(state.additionalDetails),
                onValueChange = { onEvent(UpsertMedHistoryEvent.OnAdditionalDetailsChanged(it.text)) },
                mode = mode
            )
            Spacer(modifier = Modifier.height(16.dp))
            ButtonRow(
                onSave = { onEvent(UpsertMedHistoryEvent.OnSaveClicked) },
                onDiscard = { navController.navigate("history") }
            )
        }
    }
}


@Composable
fun MedicationNameField(
    value: String,
    onValueChange: (String) -> Unit,
    mode: String
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(stringResource(id = R.string.medication_name)) },
        placeholder = { Text(stringResource(id = R.string.enter_medication_name)) },
        modifier = Modifier.fillMaxWidth(),
        readOnly = mode == "edit"
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EffectivenessDropdown(
    selectedEffectiveness: String,
    effectivenessOptions: List<String>,
    onEffectivenessChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedEffectiveness,
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
                        onEffectivenessChange(effectiveness)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun AdditionalDetailsField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    mode: String
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(stringResource(id = R.string.additional_details)) },
        placeholder = { Text(stringResource(id = R.string.enter_additional_details)) },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
        readOnly = mode == "edit"
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

fun parseDate(dateString: String): Date {
    return if (dateString.isNotEmpty()) {
        try {
            SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).parse(dateString) ?: Date()
        } catch (e: ParseException) {
            Date()
        }
    } else {
        Date()
    }
}

fun parseTime(timeString: String): Date {
    return if (timeString.isNotEmpty()) {
        try {
            SimpleDateFormat("hh:mm a", Locale.getDefault()).parse(timeString) ?: Date()
        } catch (e: ParseException) {
            Date()
        }
    } else {
        Date()
    }
}

@Preview(showBackground = true, name = "EditMedication Preview")
@Composable
fun EditMedPreview() {
    val navController = rememberNavController()
    val state = UpsertMedHistoryState()
    val onEvent: (UpsertMedHistoryEvent) -> Unit = {}
    UpsertMedicationHistoryPage(navController = navController, state = state, onEvent = onEvent)
}
