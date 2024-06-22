package com.example.dosediary

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dosediary.ui.theme.ContainerBackground
import com.example.dosediary.ui.theme.Primary

@Composable
fun MedicationListScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Header()
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            item { MedicationReminder() }
            item { DailyMedicationChecklist() }
            item { UpcomingMedicationRefills() }
            item { TomorrowsRefills() }
        }
    }
}

@Composable
fun Header() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "2024-05-06",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.End,
        )
        IconButton(onClick = { /*TODO*/ }) {
            Icon(imageVector = Icons.Outlined.Person, contentDescription = null)
        }
    }
}

@Composable
fun MedicationReminder() {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFA07A)),
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
                colors = ButtonDefaults.buttonColors(containerColor = Primary)
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
        shape = RoundedCornerShape(8.dp),
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
                        onCheckedChange = { checked ->
                            checklistItems[index] = item.first to checked
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { /*TODO*/ }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Text("View All Medication Details")
            }
        }
    }
}

@Composable
fun UpcomingMedicationRefills() {
    Card(
        shape = RoundedCornerShape(8.dp),
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
            MedicationRefillItem("Today, Jun 13, Thursday", "Medication 1", 2)
            MedicationRefillItem("Tomorrow, Jun 14, Friday", "Medication 1", 2)
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { /*TODO*/ }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Text("View All Medication Details")
            }
        }
    }
}

@Composable
fun TomorrowsRefills() {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            BasicText(
                text = "Tomorrow's Medication Refills",
                style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, fontSize = 20.sp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            MedicationRefillItem("Tomorrow, Jun 14, Friday", "Medication 1", 2)
            MedicationRefillItem("Tomorrow, Jun 14, Friday", "Medication 2", 1)
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { /*TODO*/ }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Text("View All Medication Details")
            }
        }
    }
}

@Composable
fun MedicationRefillItem(date: String, medication: String, pills: Int) {
    Column(modifier = Modifier.fillMaxWidth()) {
        BasicText(text = date, style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Info, contentDescription = null, modifier = Modifier.size(40.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                BasicText(text = medication)
                BasicText(text = "$pills Pills")
            }
            IconButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Default.Info, contentDescription = null)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MedicationScreenPreview() {
    MedicationListScreen()
}
