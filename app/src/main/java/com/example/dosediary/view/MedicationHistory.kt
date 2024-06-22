package com.example.dosediary.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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

@Composable
fun MedicationHistoryContent(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        Text("Place to add Medication History entries")
    }
}
