package com.example.dosediary.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.times
import androidx.navigation.NavController
import com.example.dosediary.components.CustomTopAppBar
import com.example.dosediary.model.Medication
import com.example.dosediary.ui.theme.ContainerBackground
import com.example.dosediary.ui.theme.Primary
import com.example.dosediary.ui.theme.Background
import com.example.dosediary.viewmodel.MedRefillState
import com.example.dosediary.viewmodel.MedRefillViewModel

@OptIn(ExperimentalMaterial3Api::class)
//@Preview
@Composable
fun MedicationRefillScreen(
    navController: NavController,
    medRefillViewModel: MedRefillViewModel
){
    val state by medRefillViewModel.state.collectAsState()

    Scaffold(
        topBar = {
            CustomTopAppBar(
                header = "Medication Refill",
                showNavigationIcon = false,
                navController = navController,
                imageResId = R.drawable.icon,  // Customizable icon
                imageDescription = "App Icon",
            )
        }
    ){ innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            BasicText(
                text = "Upcoming Medication Refills",
                style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, fontSize = 18.sp)
            )
            LazyColumn {
                item { MedicationRefillTodayList(navController, state) }
                item { MedicationRefillNextWeekList(navController, state) }
            }
        }
    }
}


@Composable
fun MedicationRefillTodayList(
    navController: NavController,
    state: MedRefillState,
    isHomePage: Boolean = false
) {
    Card(
        shape = RoundedCornerShape(35.dp),
        colors = CardDefaults.cardColors(containerColor = ContainerBackground),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            if (isHomePage) {
                BasicText(
                    text = "Upcoming Medication Refills",
                    style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, fontSize = 20.sp)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            BasicText(
                text = "Today",
                style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, fontSize = 15.sp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            // for medication in medication list render MedicationRefillDetailedItem(medication)
            if (state.medRefillsToday.isEmpty()) {
                BasicText(
                    text = "No Medication Refills Today",
                    style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, fontSize = 15.sp)
                )
            } else {
                LazyColumn(modifier = Modifier.height(min(state.medRefillsToday.size * 75.dp, 4 * 75.dp))) {
                    items(state.medRefillsToday) { medication ->
                        MedicationRefillDetailedItem(medication, onItemClick = {
                            // Navigate to MedicationRefillDetailScreen
                            navController.navigate("refillDetails/${medication.id}")
                        })
                    }
                }
            }

            if (isHomePage) {
                Button(onClick = { navController.navigate("refill") },
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    colors = ButtonDefaults.buttonColors(containerColor = Primary)
                ) {
                    Text("View All Medication Details")
                }
            }
        }
    }
}

@Composable
fun MedicationRefillNextWeekList(
    navController: NavController,
    state: MedRefillState,
) {
    Card(
        shape = RoundedCornerShape(35.dp),
        colors = CardDefaults.cardColors(containerColor = ContainerBackground),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            BasicText(
                text = "Next 7 Days",
                style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, fontSize = 15.sp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            if (state.medRefillsUpcoming.isEmpty()) {
                BasicText(
                    text = "No Medication Refills in the Next 7 Days",
                    style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, fontSize = 15.sp)
                )
            } else {
                LazyColumn(modifier = Modifier.height(min(state.medRefillsUpcoming.size * 75.dp, 4 * 75.dp))) {
                    items(state.medRefillsUpcoming) { medication ->
                        MedicationRefillDetailedItem(medication, onItemClick = {
                            // Navigate to MedicationRefillDetailScreen
                            navController.navigate("refillDetails/${medication.id}")
                        })
                    }
                }
            }
        }
    }
}

@Composable
fun MedicationRefillDetailedItem(medication: Medication, onItemClick: () -> Unit) {
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
                    text = "${medication.refillDays} Pills",
                )
            }

            val checkedState = remember { mutableStateOf(false) }
            Checkbox(
                checked = checkedState.value,
                colors = CheckboxDefaults.colors(Primary),
                onCheckedChange = { checkedState.value = it },
            )
        }
    }
}