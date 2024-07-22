package com.example.dosediary.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavController
import com.example.dosediary.R
import com.example.dosediary.components.CustomTopAppBar
import com.example.dosediary.event.MedRefillEvent
import com.example.dosediary.ui.theme.ContainerBackground
import com.example.dosediary.ui.theme.Primary
import com.example.dosediary.state.MedRefillState
import com.example.dosediary.viewmodel.ReminderViewModel

import com.example.dosediary.viewmodel.MedRefillViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(
    navController: NavController,
    state: MedRefillState,
    onEvent: (MedRefillEvent) -> Unit
) {

    // State to control the dialog visibility and message
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }
    val reminders = remember { mutableStateListOf<Pair<String,String>>() }


    // BroadcastReceiver to listen for local broadcasts
    val reminderReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val medName = intent?.getStringExtra("Name") ?: "Unknown"
            val message = intent?.getStringExtra("Message") ?: "Time to take your medication!"
            dialogMessage = "$medName: $message"
            reminders.add(Pair(medName,message))
            showDialog = true
        }
    }

    // Register the BroadcastReceiver when the Composable is first composed
    val context = LocalContext.current
    DisposableEffect(context) {
        val localBroadcastManager = LocalBroadcastManager.getInstance(context)
        localBroadcastManager.registerReceiver(reminderReceiver, IntentFilter("com.example.dosediary.REMINDER_ALERT"))

        // Cleanup when the Composable leaves the composition
        onDispose {
            localBroadcastManager.unregisterReceiver(reminderReceiver)
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("Add Medication") },
                containerColor = Color(0xFF7DCBFF)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Medication")
            }
        },
        topBar = {
            CustomTopAppBar(
                header = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                showNavigationIcon = false,
                navController = navController,
                imageResId = R.drawable.icon,  // Customizable icon
                imageDescription = "App Icon"
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
                items(reminders) { reminder ->
                    MedicationReminder(reminder) { reminders.remove(reminder) }
                }
                item { DailyMedicationChecklist() }
                item { UpcomingMedicationRefills(navController, state, onEvent) }
            }
        }
    }
}

@Composable
fun MedicationReminder(reminder: Pair<String, String>, onDismiss: () -> Unit) {
    val (medName, reminderMessage) = reminder
    val currTime = LocalDateTime.now()
    val format = DateTimeFormatter.ofPattern("hh:mm a")
    val formatTime = currTime.format(format)

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
                    text = "$reminderMessage - $formatTime",
                    style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold)
                )
                BasicText(text = medName)
            }

            Icon(painter = painterResource(R.drawable.ic_action_name),
                contentDescription = "dismiss",
                modifier = Modifier.clickable { onDismiss() })
        }
    }
}


@Composable
fun DailyMedicationChecklist() {
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
                text = "Daily Medication Checklist",
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
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { /*TODO*/ },
                modifier = Modifier.align(Alignment.CenterHorizontally),
                colors = ButtonDefaults.buttonColors(containerColor = Primary)
            ) {
                Text("View All Medication Details")
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
    Card(
        shape = RoundedCornerShape(35.dp),
        colors = CardDefaults.cardColors(containerColor = ContainerBackground),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        MedicationRefillTodayList(navController, state, onEvent, true)
    }
}