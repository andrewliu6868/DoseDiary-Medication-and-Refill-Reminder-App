package com.example.dosediary

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon

@Composable
fun MedicationHistory(onNavigateToAddMedication: () -> Unit) {
    DoseDiaryTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = { MedicationHistoryTopBar(onAddClicked = onNavigateToAddMedication) },
            content = { innerPadding ->
                MedicationHistoryContent(Modifier.padding(innerPadding))
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicationHistoryTopBar(onAddClicked: () -> Unit) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = { Text("Medication History") },
        actions = {
            IconButton(onClick = onAddClicked) {
                Icon(Icons.Filled.Add, contentDescription = "Add Medication")
            }
        }
    )
}

@Composable
fun MedicationHistoryContent(modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Text("Place to add Medication History entries")
    }
}
