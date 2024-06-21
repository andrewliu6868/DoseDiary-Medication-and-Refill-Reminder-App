package com.example.dosediary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.DoseDiary.ui.theme.DoseDiaryTheme
import com.example.DoseDiary.R
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Card

data class Medication(
    val name: String,
    val info: String
)

class MedicationRefillActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DoseDiaryTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Upcoming Medication Refills") },
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
                        Medication("Medication 2", "Subhead"),
                        Medication("Medication 3", "Subhead"),
                        Medication("Medication 4", "Subhead")
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    MedicationList(medicationList, innerPadding)
                }
            }
        }
    }
}

@Composable
fun MedicationList(medications: List<Medication>, innerPadding: PaddingValues) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(medications) { medication:Medication ->
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

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DoseDiaryTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Upcoming Medication Refills") },
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
                Medication("Medication 2", "Subhead"),
                Medication("Medication 3", "Subhead"),
                Medication("Medication 4", "Subhead")
            )
            Spacer(modifier = Modifier.height(16.dp))
            MedicationList(medicationList, innerPadding)
        }
    }
}