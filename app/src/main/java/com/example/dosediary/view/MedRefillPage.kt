package com.example.dosediary.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.AlertDialog
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.navigation.NavController
import com.example.dosediary.R
import com.example.dosediary.components.CustomTopAppBar
import com.example.dosediary.event.MedRefillEvent
import com.example.dosediary.state.MedRefillState
import com.example.dosediary.state.MedicationWithNextRefillDate
import com.example.dosediary.ui.theme.Background
import com.example.dosediary.ui.theme.ContainerBackground
import com.example.dosediary.ui.theme.Primary
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
//@Preview
@Composable
fun MedicationRefillPage(
    navController: NavController,
    state: MedRefillState,
    onEvent: (MedRefillEvent) -> Unit
) {
    Scaffold(
        topBar = {
            CustomTopAppBar(
                header = stringResource(R.string.medication_refill),
                showNavigationIcon = false,
                navController = navController,
                imageResId = R.drawable.icon,  // Customizable icon
                imageDescription = stringResource(R.string.app_icon),
            )
        }
    ){ innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            BasicText(
                text = stringResource(R.string.upcoming_medication_refills),
                style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, fontSize = 18.sp)
            )
            LazyColumn {
                item { MedicationRefillTodayList(navController, state, onEvent) }
                item { MedicationRefillNextWeekList(navController, state, onEvent) }
            }
        }

        if (state.showRefillConfirmationDialog) {
            RefillConfirmationDialog(
                onConfirm = { onEvent(MedRefillEvent.ConfirmRefillMedication) },
                onDismiss = { onEvent(MedRefillEvent.DismissRefillDialog) }
            )
        }
    }
}


@Composable
fun MedicationRefillTodayList(
    navController: NavController,
    state: MedRefillState,
    onEvent: (MedRefillEvent) -> Unit,
    isHomePage: Boolean = false
) {
    Card(
        shape = RoundedCornerShape(35.dp),
        colors = CardDefaults.cardColors(containerColor = ContainerBackground),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            if (isHomePage) {
                BasicText(
                    text = stringResource(R.string.upcoming_medication_refills),
                    style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, fontSize = 20.sp)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (state.medRefillsToday.isEmpty()) {
                BasicText(
                    text = stringResource(R.string.no_medication_refills_today),
                    style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, fontSize = 15.sp)
                )
            } else {
                BasicText(
                    text = stringResource(R.string.today),
                    style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, fontSize = 15.sp)
                )
                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(modifier = Modifier.height(min(state.medRefillsToday.size * 75.dp, 4 * 75.dp))) {
                    items(state.medRefillsToday) { medication ->
                        onEvent(MedRefillEvent.SetSelectedRefillDetail(medication))
                        MedicationRefillDetailedItem(medication, onItemClick = {
                            // Navigate to MedicationRefillDetailScreen
                            navController.navigate("refillDetails")
                        }, onEvent)
                    }
                }
            }
        }
    }
}

@Composable
fun RefillConfirmationDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(R.string.confirm_refill)) },
        text = { Text(text = stringResource(R.string.confirm_refill_message)) },
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
fun MedicationRefillNextWeekList(
    navController: NavController,
    state: MedRefillState,
    onEvent: (MedRefillEvent) -> Unit,
) {
    Card(
        shape = RoundedCornerShape(35.dp),
        colors = CardDefaults.cardColors(containerColor = ContainerBackground),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            BasicText(
                text = stringResource(R.string.next_7_days),
                style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, fontSize = 15.sp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            if (state.medRefillsUpcoming.isEmpty()) {
                BasicText(
                    text = stringResource(R.string.no_medication_refills_next_week),
                    style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, fontSize = 15.sp)
                )
            } else {
                LazyColumn(modifier = Modifier.height(min(state.medRefillsUpcoming.size * 75.dp, 4 * 75.dp))) {
                    items(state.medRefillsUpcoming) { medication ->
                        MedicationRefillDetailedItem(medication, onItemClick = {
                            // Navigate to MedicationRefillDetailScreen
//                            navController.navigate("refillDetails/${medication.medication.id}")
                            onEvent(MedRefillEvent.SetSelectedRefillDetail(medication))
                            navController.navigate("refillDetails")
                        }, onEvent)
                    }
                }
            }
        }
    }
}

@Composable
fun MedicationRefillDetailedItem(
    medicationWithNextRefillDate: MedicationWithNextRefillDate,
    onItemClick: () -> Unit,
    onEvent: (MedRefillEvent) -> Unit,
) {
//    val shouldCheck = shouldCheckCheckbox(
//        medicationWithNextRefillDate.medication.lastRefilledDate,
//        medicationWithNextRefillDate.medication.refillDays
//    )
//    val checkedState = remember { mutableStateOf(shouldCheck) }
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())


    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        colors = CardDefaults.cardColors(containerColor = Background),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable {
                onItemClick()
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(8.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.icon),
                contentDescription = stringResource(R.string.pill_pics),
                modifier = Modifier.width(40.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                BasicText(
                    text = medicationWithNextRefillDate.medication.medicationName,
                )
                BasicText(
                    text = "${dateFormat.format(medicationWithNextRefillDate.nextRefillDate)}",
                )
            }

            Checkbox(
                checked = false,
                colors = CheckboxDefaults.colors(Primary),
                onCheckedChange = {
                    onEvent(MedRefillEvent.SetRefillCompleted(medicationWithNextRefillDate))
                },
            )
        }
    }
}

//fun shouldCheckCheckbox(lastRefillDate: Date, refillDays: Int): Boolean {
//    val calendar = Calendar.getInstance()
//    // if last refill date is null, use todays date
//    calendar.time = lastRefillDate
//    calendar.add(Calendar.DAY_OF_YEAR, refillDays)
//    val suggestedRefillDate = calendar.time
//    val currentDate = Date()
//    return currentDate >= suggestedRefillDate
//}