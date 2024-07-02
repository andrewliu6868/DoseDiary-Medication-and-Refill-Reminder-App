package com.example.dosediary.view

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.dosediary.ui.theme.DoseDiaryTheme
import com.example.dosediary.model.Medicine
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dosediary.ui.theme.Background
import androidx.navigation.NavHostController
import com.example.dosediary.components.Header

import com.example.dosediary.ui.theme.ContainerBackground

@Composable
fun MedicationHistory(navController: NavHostController) {
    val showEditMedication = remember { mutableStateOf(false) }

    DoseDiaryTheme {
        if (showEditMedication.value) {
            EditMedication(navController)
        } else {
            Scaffold(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                containerColor = Background,
                topBar = {
                    MedicationHistoryTopBar(
                        onAddClicked = { showEditMedication.value = true },
                    )
                },
                content = { innerPadding ->
                    MedicationHistoryContent(Modifier.padding(innerPadding), navController)
                },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicationHistoryTopBar(onAddClicked: () -> Unit) {
    val context = LocalContext.current
    Header(text = "Medication History")

//    TopAppBar(
//        modifier = Modifier.height(55.dp),
//        colors = TopAppBarDefaults.topAppBarColors(
//            containerColor = Background,
//            titleContentColor = Color.Black,
//        ),
//        title = {
//            Box(
//                modifier = Modifier.fillMaxWidth()
//                .padding(vertical = 8.dp),
//            contentAlignment = Alignment.Center
//            ){
//                Text("Medication History",
//                    fontSize = 25.sp,
//                    fontWeight = FontWeight.Bold,
//                    textAlign = TextAlign.End,)
//            }
//        },
//        actions = {
//            IconButton(onClick = onAddClicked) {
//                Icon(Icons.Filled.Add, contentDescription = "Add Medication")
//            }
//            IconButton(onClick = { generatePDF(context, sampleMedications) }) {
//                Icon(Icons.Filled.FileDownload, contentDescription = "Generate Report")
//            }
//        }
//    )
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
fun MedicationHistoryContent(modifier: Modifier = Modifier, navController: NavHostController) {
    LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .background(ContainerBackground),
            contentPadding = PaddingValues(bottom = 20.dp)
    ) {
        items(sampleMedications) { medication ->
            MedicationItem(medication, navController)
        }
    }
}

@Composable
fun MedicationItem(medication: Medicine, navController: NavHostController) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Background,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        )
    ) {Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)  // This makes the column take up all space except for the button
        ) {
            Text(text = medication.name,
                style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, fontSize = 15.sp))
            Text(text = "Taken: ${medication.timeTaken}",
                style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, fontSize = 15.sp))
            Text(text = medication.effectiveness,
                style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, fontSize = 15.sp))
        }
        IconButton(onClick = {navController.navigate("editMedication")}) {
            Icon(Icons.Filled.Edit, contentDescription = "Edit")
        }
    }
    }
}




