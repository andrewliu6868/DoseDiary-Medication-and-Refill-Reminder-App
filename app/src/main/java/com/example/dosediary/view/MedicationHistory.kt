package com.example.dosediary.view

import android.content.Context
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.dosediary.ui.theme.DoseDiaryTheme
import com.example.dosediary.model.Medicine
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dosediary.ui.theme.Background
import androidx.navigation.NavHostController


@Composable
fun MedicationHistory(navController: NavHostController) {
    val showEditMedication = remember { mutableStateOf(false) }

    DoseDiaryTheme {
        if (showEditMedication.value) {
            EditMedication(navController)
        } else {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    MedicationHistoryTopBar(
                        onAddClicked = { showEditMedication.value = true },
                        onBackClicked = {
                            //navigate to MedicationListScreen
                        }
                    )
                },
                content = { innerPadding ->
                    MedicationHistoryContent(Modifier.padding(innerPadding))
                },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicationHistoryTopBar(onAddClicked: () -> Unit, onBackClicked: () -> Unit ) {
    val context = LocalContext.current

    TopAppBar(
        modifier = Modifier.height(40.dp),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Background,
            titleContentColor = Color.Black,
        ),
        navigationIcon = {
            IconButton(onClick = onBackClicked) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Go Back")
            }
        },
        title = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ){
                Text("Medication History",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.End,)
            }
        },
        actions = {
            IconButton(onClick = onAddClicked) {
                Icon(Icons.Filled.Add, contentDescription = "Add Medication")
            }
            IconButton(onClick = { generatePDF(context, sampleMedications) }) {
                Icon(Icons.Filled.FileDownload, contentDescription = "Generate Report")
            }
        }
    )
}

fun generatePDF(context: Context, medications: List<Medicine>) {}

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
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 10.dp),
            contentPadding = PaddingValues(bottom = 20.dp)
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
            Text(text = medication.name, 
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End,)
            Text(text = "Taken: ${medication.timeTaken}",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End,)
            Text(text = " ${medication.effectiveness}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End,)
        }
    }

}




