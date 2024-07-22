package com.example.dosediary.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.dosediary.R
import com.example.dosediary.components.CustomTopAppBar
import com.example.dosediary.event.MedRefillEvent
import com.example.dosediary.ui.theme.ContainerBackground
import com.example.dosediary.ui.theme.Primary
import com.example.dosediary.state.MedRefillState

import com.example.dosediary.viewmodel.MedRefillViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun HomePage(
    navController: NavController,
    state: MedRefillState,
    onEvent: (MedRefillEvent) -> Unit
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
                item { DailyMedicationChecklist(navController) }
                item { UpcomingMedicationRefills(navController, state, onEvent) }
            }
            if (state.showRefillConfirmationDialog) {
                RefillConfirmationDialog(
                    onConfirm = { onEvent(MedRefillEvent.ConfirmRefillMedication) },
                    onDismiss = { onEvent(MedRefillEvent.DismissRefillDialog) }
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
                        style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold)
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
fun DailyMedicationChecklist(navController: NavController) {
    // Manage the state of each checkbox
    val checklistItems = remember {
        mutableStateListOf(
            "Tylenol - 1:00 pm" to true,
            "Tylenol - 2:00 pm" to true,
            "Tylenol - 3:00 pm" to true,
            "Tylenol - 4:00 pm" to true,
            "Tylenol - 5:00 pm" to true,
            "Tylenol - 6:00 pm" to true
        )
    }

    Card(
        shape = RoundedCornerShape(35.dp),
        colors = CardDefaults.cardColors(containerColor = ContainerBackground),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            BasicText(
                text = stringResource(R.string.daily_medication_checklist),
                style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, fontSize = 20.sp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            checklistItems.forEachIndexed { index, item ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BasicText(
                        text = item.first,
                        modifier = Modifier.weight(1f)
                    )
                    Checkbox(
                        checked = item.second,
                        colors = CheckboxDefaults.colors(Primary),
                        onCheckedChange = { checked ->
                            checklistItems[index] = item.first to checked
                        }
                    )
                }
            }
        }
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