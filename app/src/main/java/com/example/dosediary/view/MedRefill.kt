package com.example.dosediary.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
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
import androidx.navigation.NavController
import com.example.dosediary.components.CustomTopAppBar
import com.example.dosediary.model.Medication
import com.example.dosediary.ui.theme.ContainerBackground
import com.example.dosediary.ui.theme.Primary
import com.example.dosediary.ui.theme.Background
import com.example.dosediary.viewmodel.MedRefillState
import com.example.dosediary.viewmodel.MedRefillViewModel

data class MedicationRefill(
    val medicationName: String,
    val dosage: String,
    val quantity: Int,
    val refillQuantity: Int,
    val refillDate: String, // Could use LocalDate for date handling
    val nextRefillDate: String, // Could use LocalDate for date handling
    val refillFrequency: Int, // Number of days between refills
    val pharmacyName: String,
    val pharmacyContact: String,
    val pharmacyAddress: String,
    val prescriptionNumber: String,
    val prescribingDoctor: String,
    val doctorContact: String,
    val reminderSettings: String, // Could use a more complex type for detailed reminders
    val currentStock: Int,
    val insuranceInformation: String,
    val notes: String
)

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
                imageDescription = "App Icon"
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

//val medicationRefill1 = MedicationRefill(
//    medicationName = "Lisinopril",
//    dosage = "10 mg",
//    quantity = 90,
//    refillQuantity = 90,
//    refillDate = "2024-06-01",
//    nextRefillDate = "2024-09-01",
//    refillFrequency = 90, // Every 90 days
//    pharmacyName = "Pharmacy One",
//    pharmacyContact = "555-1234",
//    pharmacyAddress = "123 Main St, Springfield",
//    prescriptionNumber = "RX123456",
//    prescribingDoctor = "Dr. John Smith",
//    doctorContact = "555-5678",
//    reminderSettings = "2 days before refill date",
//    currentStock = 30,
//    insuranceInformation = "Insurance Company A",
//    notes = "Take with food."
//)
//
//val medicationRefill2 = MedicationRefill(
//    medicationName = "Metformin",
//    dosage = "500 mg",
//    quantity = 60,
//    refillQuantity = 60,
//    refillDate = "2024-06-15",
//    nextRefillDate = "2024-08-15",
//    refillFrequency = 60, // Every 60 days
//    pharmacyName = "Pharmacy Two",
//    pharmacyContact = "555-2345",
//    pharmacyAddress = "456 Oak St, Springfield",
//    prescriptionNumber = "RX654321",
//    prescribingDoctor = "Dr. Jane Doe",
//    doctorContact = "555-6789",
//    reminderSettings = "3 days before refill date",
//    currentStock = 20,
//    insuranceInformation = "Insurance Company B",
//    notes = "Avoid alcohol."
//)
//
//val medicationRefill3 = MedicationRefill(
//    medicationName = "Atorvastatin",
//    dosage = "20 mg",
//    quantity = 30,
//    refillQuantity = 30,
//    refillDate = "2024-06-20",
//    nextRefillDate = "2024-07-20",
//    refillFrequency = 30, // Every 30 days
//    pharmacyName = "Pharmacy Three",
//    pharmacyContact = "555-3456",
//    pharmacyAddress = "789 Pine St, Springfield",
//    prescriptionNumber = "RX789012",
//    prescribingDoctor = "Dr. Emily Brown",
//    doctorContact = "555-7890",
//    reminderSettings = "1 day before refill date",
//    currentStock = 10,
//    insuranceInformation = "Insurance Company C",
//    notes = "Take in the evening."
//)
//
//val medicationRefill4 = MedicationRefill(
//    medicationName = "Lisinopril",
//    dosage = "10 mg",
//    quantity = 90,
//    refillQuantity = 90,
//    refillDate = "2024-06-01",
//    nextRefillDate = "2024-09-01",
//    refillFrequency = 90, // Every 90 days
//    pharmacyName = "Pharmacy One",
//    pharmacyContact = "555-1234",
//    pharmacyAddress = "123 Main St, Springfield",
//    prescriptionNumber = "RX123456",
//    prescribingDoctor = "Dr. John Smith",
//    doctorContact = "555-5678",
//    reminderSettings = "2 days before refill date",
//    currentStock = 30,
//    insuranceInformation = "Insurance Company A",
//    notes = "Take with food."
//)
//
//val medicationList = listOf(
//    medicationRefill1,
//    medicationRefill2,
//    medicationRefill3,
//    medicationRefill4
//)
@Composable
fun MedicationRefillTodayList(
    navController: NavController,
    state: MedRefillState
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
                text = "Today",
                style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, fontSize = 15.sp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            // for medication in medication list render MedicationRefillDetailedItem(medication)
            LazyColumn(modifier = Modifier.height(240.dp)) {
                items(state.medRefills) { medication ->
                    MedicationRefillDetailedItem(medication, onItemClick = {
                        // Navigate to MedicationRefillDetailScreen
                        navController.navigate("refillDetails")
                    })
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
            LazyColumn(modifier = Modifier.height(240.dp)) {
                items(state.medRefills) { medication ->
                    MedicationRefillDetailedItem(medication, onItemClick = {
                        // Navigate to MedicationRefillDetailScreen
                        navController.navigate("refillDetails")
                    })
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