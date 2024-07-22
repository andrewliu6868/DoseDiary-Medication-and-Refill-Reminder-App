package com.example.dosediary.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dosediary.R
import com.example.dosediary.components.CustomTopAppBar
import com.example.dosediary.event.MedRefillEvent
import com.example.dosediary.model.entity.Medication
import com.example.dosediary.model.entity.MedicationHistory
import com.example.dosediary.state.MedRefillState
import com.example.dosediary.state.MedicationListState
import com.example.dosediary.ui.theme.ContainerBackground
import com.example.dosediary.ui.theme.Primary
import com.example.dosediary.viewmodel.MedicationHistoryViewModel
import com.example.dosediary.viewmodel.MedicationListViewModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(
    navController: NavController,
    Liststate: MedicationListState,
    Refillstate: MedRefillState,
    ListonEvent: MedicationListViewModel,
    RefillonEvent: (MedRefillEvent) -> Unit,
    HistoryonEvent: MedicationHistoryViewModel
) {

    Scaffold(
        topBar = {
            CustomTopAppBar(
                header = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                showNavigationIcon = false,
                navController = navController,
                imageResId = R.drawable.icon,  // Customizable icon
                imageDescription = stringResource(R.string.app_icon)
            )
        }
    ){ padding -> Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(padding)
            .padding(horizontal = 16.dp)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            item { MedicationReminder() }
            item{DailyMedicationChecklist(ListonEvent, HistoryonEvent)}
            item { UpcomingMedicationRefills(navController, Refillstate, RefillonEvent) }
        }
        if (Refillstate.showRefillConfirmationDialog) {
            RefillConfirmationDialog(
                onConfirm = { RefillonEvent(MedRefillEvent.ConfirmRefillMedication) },
                onDismiss = { RefillonEvent(MedRefillEvent.DismissRefillDialog) }
            )
        }
    }
    }
}

@Composable
fun MedicationReminder() {
    var isVisible = false
    if (isVisible){
        Card(
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFF7676)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    BasicText(
                        text = stringResource(R.string.medication_reminder_time, "7:00pm"),
                    )
                    BasicText(text = "Tylenol")
                }
                Button(
                    onClick = { /*TODO*/ },
                    colors = ButtonDefaults.buttonColors(containerColor = Primary),
                ) {
                    Text(stringResource(R.string.view))
                }
            }
        }
    }
}

@Composable
fun DailyMedicationChecklist(viewModel: MedicationListViewModel, historyMode: MedicationHistoryViewModel) {
    val medicationList by viewModel.state.collectAsState()
    val dateFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
    var showDialog by remember { mutableStateOf<Pair<Medication, Date>?>(null) }

    Card(
        shape = RoundedCornerShape(35.dp),
        colors = CardDefaults.cardColors(containerColor = ContainerBackground),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Daily Medication Checklist",
                style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, fontSize = 20.sp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            Box(modifier = Modifier.fillMaxWidth()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp)
                ) {
                    items(
                        medicationList.medicationList.filter { it.endDate.after(Date()) }
                            .flatMap { medication ->
                                medication.times.map { time ->
                                    medication to time
                                }
                            }
                            .sortedBy { it.second }
                    ) { (medication, time) ->
                        val checked = medication.takenTimes[time] ?: false
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${medication.medicationName} - ${dateFormat.format(time)}"
                            )
                            Spacer(modifier = Modifier.weight(1f))

                            Checkbox(
                                checked = checked,
                                onCheckedChange = {

                                    // Convert Date to LocalDateTime
                                    val localDateTime = time.toInstant()
                                        .atZone(ZoneId.systemDefault())
                                        .toLocalDateTime()

                                    // Format the day as MM-DD-YYYY
                                    val dayFormatted = localDateTime.format(DateTimeFormatter.ofPattern("MM-dd-yyyy"))

                                    val timeFormatted = localDateTime.format(DateTimeFormatter.ofPattern("HH:mm"))


                                    showDialog = medication to time

                                    var historyItem = MedicationHistory(
                                        medication.id,
                                        medication.medicationName,
                                        dayFormatted,
                                        timeFormatted,
                                        "Effective",
                                        medication.owner,
                                        "")
                                    historyMode.addOrUpdateMedicationHistory(historyItem)
                                },
                                colors = CheckboxDefaults.colors(checkedColor = Primary)
                            )
                        }
                    }
                }
            }
        }
    }

    showDialog?.let { (medication, time) ->
        AlertDialog(
            onDismissRequest = { showDialog = null },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.updateTakenStatus(medication.id, time, true)
                        showDialog = null
                    }
                ) {
                    Text("Yes", color = Primary)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDialog = null }
                ) {
                    Text("No", color = Primary)
                }
            },
            title = {
                Text(
                    text = "Confirm Taken",
                    style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, fontSize = 18.sp)
                )
            },
            text = {
                Text("Are you sure the medication was taken?")
            },
            modifier = Modifier.background(ContainerBackground, shape = RoundedCornerShape(16.dp))
        )
    }
}

@Composable
fun UpcomingMedicationRefills(
    navController: NavController,
    state: MedRefillState,
    onEvent: (MedRefillEvent) -> Unit
) {

    MedicationRefillTodayList(navController, state, onEvent, true)

}