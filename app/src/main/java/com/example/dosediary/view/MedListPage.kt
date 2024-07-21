package com.example.dosediary.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dosediary.R
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.times
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.dosediary.components.CustomTopAppBar
import com.example.dosediary.event.MedRefillEvent
import com.example.dosediary.event.MedicationListEvent
import com.example.dosediary.events.AddMedicationEvent
import com.example.dosediary.model.entity.Medication
import com.example.dosediary.ui.theme.ContainerBackground
import com.example.dosediary.ui.theme.Primary
import com.example.dosediary.ui.theme.Background
import com.example.dosediary.state.MedRefillState
import com.example.dosediary.state.MedicationListState

import com.example.dosediary.state.MedicationWithNextRefillDate
import com.example.dosediary.viewmodel.MedRefillViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
//@Preview
@Composable
fun MedicationListPage(
    navController: NavController,
    state: MedicationListState,
    onEvent: (MedicationListEvent) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("Add Medication") },
                containerColor = Color(0xFF7DCBFF)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Medication")
            }
        },
        topBar = {
            CustomTopAppBar(
                header = "Medication List",
                showNavigationIcon = false,
                navController = navController,
                imageResId = R.drawable.icon,  // Customizable icon
                imageDescription = "App Icon",
            )
        }
    ){ innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            items(state.medicationList) { medication ->
                MedicationItem(medication, onItemClick = {
                    // Navigate to Edit Medication
                    onEvent(MedicationListEvent.SelectMedication(medication))
                    navController.navigate("Edit Medication")
                })
            }
        }
    }
}

@Composable
fun MedicationItem(
    medication: Medication,
    onItemClick: () -> Unit,
) {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        colors = CardDefaults.cardColors(containerColor = Background),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable {
                onItemClick()
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(8.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.icon),
                contentDescription = "Pill Pics",
                modifier = Modifier.width(40.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                BasicText(
                    text = medication.medicationName,
                )
                BasicText(
                    text = "${dateFormat.format(medication.startDate)}",
                )
            }
        }
    }
}


