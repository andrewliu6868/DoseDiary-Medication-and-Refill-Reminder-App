package com.example.dosediary.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.DoseDiary.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Medication(
    val name: String,
    val info: String
)
@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun MedicationRefillScreen() {
    Scaffold(
        topBar = {
            val currentDate = Date()
            val formatter = SimpleDateFormat("MMMM dd", Locale.getDefault())
            val formattedDate = formatter.format(currentDate)

            TopAppBar(
                //set title to be today date
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center

                    ) {
                        Text(formattedDate)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Filled.Menu, contentDescription = null)
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Filled.AccountCircle, contentDescription = null)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { }) {
                Icon(Icons.Filled.Add, contentDescription = null)
            }
        }
    ) { innerPadding ->
        val medicationList = listOf(
            Medication("Medication 1", "2 Pills"),
            Medication("Medication 2", "3 Pills Left"),
            Medication("Medication 3", "Subhead"),
            Medication("Medication 4", "Subhead")
        )

        Column(modifier = Modifier.padding(innerPadding).padding(horizontal = 16.dp)) {
            Text(
                text = "Upcoming Medication Refills"
            )
            MedicationList(medicationList)
        }

    }
}

@Composable
fun MedicationList(medications: List<Medication>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(medications) { medication: Medication ->
            MedicationItem(medication)
        }
    }
}

@Composable
fun MedicationItem(medication: Medication) {
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(8.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_medication),
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = medication.name,
                    fontSize = 20.sp,
                    color = Color.Black
                )
                Text(
                    text = medication.info,
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }
            IconButton(onClick = { }) {
                Icon(Icons.Filled.Edit, contentDescription = null)
            }
            IconButton(onClick = { }) {
                Icon(Icons.Filled.Delete, contentDescription = null)
            }
        }
    }
}