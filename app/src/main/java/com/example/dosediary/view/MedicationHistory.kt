package com.example.dosediary.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.DoseDiary.ui.theme.DoseDiaryTheme
import com.example.DoseDiary.ui.theme.Primary
import com.example.dosediary.model.Medicine
import com.example.dosediary.ui.theme.DoseDiaryTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp


@Composable
fun MedicationHistory() {
    val showEditMedication = remember { mutableStateOf(false) }

    DoseDiaryTheme {
        if (showEditMedication.value) {
            EditMedication()
        } else {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = { MedicationHistoryTopBar { showEditMedication.value = true } },
                content = { innerPadding ->
                    MedicationHistoryContent(Modifier.padding(innerPadding))
                },
                bottomBar = {
                    Button(
                        onClick = { /* Handle generate report */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Primary
                        )
                    ) {
                        Text("Generate Report")
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicationHistoryTopBar(onAddClicked: () -> Unit) {
    TopAppBar(
        modifier = Modifier.height(60.dp),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.TopCenter
            ){
                Text("Medication History")
            }
        },
        actions = {
            IconButton(onClick = onAddClicked) {
                Icon(Icons.Filled.Add, contentDescription = "Add Medication")
            }
        }
    )
}

val sampleMedications = listOf(
    Medicine("Ibuprofen", "Wednesday 6:00 PM", "Effective"),
    Medicine("Amoxicillin", "Wednesday 7:30 PM", "Effective"),
    Medicine("Amoxicillin", "Wednesday 7:30 PM", "Effective"),
    Medicine("Amoxicillin", "Wednesday 7:30 PM", "Effective"),
    Medicine("Amoxicillin", "Wednesday 7:30 PM", "Effective"),
    Medicine("Amoxicillin", "Wednesday 7:30 PM", "Effective"),
    Medicine("Oxaprozin", "Thursday 5:00 AM", "Effective")
)

@Composable
fun MedicationHistoryContent(modifier: Modifier = Modifier) {
    LazyColumn(
            modifier = modifier.fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            contentPadding = PaddingValues(bottom = 60.dp)
    ) {
        items(sampleMedications) { medication ->
            MedicationItem(medication)
        }
    }
}

@Composable
fun MedicationItem(medication: Medicine) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(text = medication.name, style = MaterialTheme.typography.titleLarge)
            Text(text = "Taken: ${medication.timeTaken}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Effectiveness: ${medication.effectiveness}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}


