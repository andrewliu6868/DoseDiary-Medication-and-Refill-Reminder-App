package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicationRefillEditScreen(navController: NavController, medicationName: String?) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Medication") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Text(text = "Editing: $medicationName", fontSize = 20.sp)
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = medicationName ?: "",
                onValueChange = { /* Handle text change */ },
                label = { Text("Medication Name") }
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = "",
                onValueChange = { /* Handle text change */ },
                label = { Text("Medication Info") }
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { /* Handle save */ }) {
                Text("Save")
            }
        }
    }
}

@Preview
@Composable
fun MedicationEditScreenPreview() {
    // Preview the MedicationEditScreen
    val navController = rememberNavController()
    MedicationRefillEditScreen(navController = navController, medicationName = "Medication Name")
}
