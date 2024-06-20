package com.example.dosediary

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditMedication() {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Edit Medication") }) },
        content = { /* Content for editing or adding medication */ }
    )
}