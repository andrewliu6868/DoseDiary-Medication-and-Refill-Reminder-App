package com.example.dosediary.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dosediary.R
import com.example.dosediary.components.Header
import com.example.dosediary.ui.theme.ContainerBackground
import com.example.dosediary.ui.theme.Background
import com.example.dosediary.ui.theme.Primary
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MedicationListScreen(navController: NavController) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("Add Medication") },
                containerColor = Color(0xFF7DCBFF)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Medication")
            }
        }
    ){ padding -> Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(padding)
        ) {
            HomeHeader()
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                item { MedicationReminder() }
                item { DailyMedicationChecklist() }
                item { UpcomingMedicationRefills() }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeHeader() {
    val currentDate = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val formattedDate = currentDate.format(formatter)
    Header(text = formattedDate)
}

@Composable
fun MedicationReminder() {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFF7676)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                BasicText(
                    text = "Medication Reminder - 7:00pm",
                    style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold)
                )
                BasicText(text = "Tylenol")
            }
            Button(
                onClick = { /*TODO*/ },
                colors = ButtonDefaults.buttonColors(containerColor = Primary),
            ) {
                Text("View")
            }
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
//            "Tylenol - 7:00 pm" to true,
//            "Tylenol - 8:00 pm" to true,
//            "Tylenol - 9:00 pm" to true,
//            "Tylenol - 10:00 pm" to true,
//            "Tylenol - 11:00 pm" to true,
//            "Tylenol - 12:00 pm" to true
        )
    }

    Card(
        shape = RoundedCornerShape(35.dp),
        colors = CardDefaults.cardColors(containerColor = ContainerBackground),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
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
fun UpcomingMedicationRefills() {
    Card(
        shape = RoundedCornerShape(35.dp),
        colors = CardDefaults.cardColors(containerColor = ContainerBackground),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            BasicText(
                text = "Upcoming Medication Refills",
                style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, fontSize = 20.sp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            BasicText(text = "Today, Jun 13, Thursday", style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold))
            Spacer(modifier = Modifier.height(4.dp))
            MedicationRefillDetailedItem(medicationRefill1 ,{})
            Spacer(modifier = Modifier.height(8.dp))

            BasicText(text = "Tomorrow, Jun 14, Friday", style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold))
            Spacer(modifier = Modifier.height(4.dp))
            MedicationRefillDetailedItem(medicationRefill2 ,{})
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
fun MedicationRefillItem(date: String, medication: String, pills: Int) {
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        colors = CardDefaults.cardColors(containerColor = Background),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable {
//                onItemClick()
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.icon),
                contentDescription = "Pill Pics",
                modifier = Modifier.width(30.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                BasicText(text = medication)
                BasicText(text = "$pills Pills")
            }
            IconButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Outlined.Info, contentDescription = null)
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun MedicationScreenPreview() {
//    MedicationListScreen()
//}
