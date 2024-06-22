package com.example.dosediary.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.DoseDiary.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

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
@Preview
@Composable
fun MedicationRefillScreen() {
    Scaffold(
        topBar = {
            val currentDate = Date()
            val formatter = SimpleDateFormat("MMMM dd", Locale.getDefault())
            val formattedDate = formatter.format(currentDate)

            TopAppBar(
                //set title to be today date
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center

                    ) {
                        Text(formattedDate)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Filled.Menu, contentDescription = null)
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Filled.AccountCircle, contentDescription = null)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { }) {
                Icon(Icons.Filled.Add, contentDescription = null)
            }
        }
    ) { innerPadding ->

        val medicationRefill1 = MedicationRefill(
            medicationName = "Lisinopril",
            dosage = "10 mg",
            quantity = 90,
            refillQuantity = 90,
            refillDate = "2024-06-01",
            nextRefillDate = "2024-09-01",
            refillFrequency = 90, // Every 90 days
            pharmacyName = "Pharmacy One",
            pharmacyContact = "555-1234",
            pharmacyAddress = "123 Main St, Springfield",
            prescriptionNumber = "RX123456",
            prescribingDoctor = "Dr. John Smith",
            doctorContact = "555-5678",
            reminderSettings = "2 days before refill date",
            currentStock = 30,
            insuranceInformation = "Insurance Company A",
            notes = "Take with food."
        )

        val medicationRefill2 = MedicationRefill(
            medicationName = "Metformin",
            dosage = "500 mg",
            quantity = 60,
            refillQuantity = 60,
            refillDate = "2024-06-15",
            nextRefillDate = "2024-08-15",
            refillFrequency = 60, // Every 60 days
            pharmacyName = "Pharmacy Two",
            pharmacyContact = "555-2345",
            pharmacyAddress = "456 Oak St, Springfield",
            prescriptionNumber = "RX654321",
            prescribingDoctor = "Dr. Jane Doe",
            doctorContact = "555-6789",
            reminderSettings = "3 days before refill date",
            currentStock = 20,
            insuranceInformation = "Insurance Company B",
            notes = "Avoid alcohol."
        )

        val medicationRefill3 = MedicationRefill(
            medicationName = "Atorvastatin",
            dosage = "20 mg",
            quantity = 30,
            refillQuantity = 30,
            refillDate = "2024-06-20",
            nextRefillDate = "2024-07-20",
            refillFrequency = 30, // Every 30 days
            pharmacyName = "Pharmacy Three",
            pharmacyContact = "555-3456",
            pharmacyAddress = "789 Pine St, Springfield",
            prescriptionNumber = "RX789012",
            prescribingDoctor = "Dr. Emily Brown",
            doctorContact = "555-7890",
            reminderSettings = "1 day before refill date",
            currentStock = 10,
            insuranceInformation = "Insurance Company C",
            notes = "Take in the evening."
        )
        val medicationList = listOf(
            medicationRefill1,
            medicationRefill2,
            medicationRefill3
        )

        Column(modifier = Modifier.padding(innerPadding).padding(horizontal = 16.dp)) {
            Text(
                text = "Upcoming Medication Refills"
            )
            MedicationList(medicationList)
        }

    }
}

@Composable
fun MedicationList(medications: List<MedicationRefill>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(medications) { medication: MedicationRefill ->
            MedicationItem(medication)
        }
    }
}

@Composable
fun MedicationItem(medication: MedicationRefill) {
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(8.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_medication),
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = medication.medicationName,
                    fontSize = 20.sp,
                    color = Color.Black
                )
                Text(
                    text = "Refill date: " + medication.nextRefillDate,
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }
            IconButton(onClick = { }) {
                Icon(Icons.Filled.Edit, contentDescription = null)
            }
//            IconButton(onClick = { }) {
//                Icon(Icons.Filled.Delete, contentDescription = null)
//            }
            val checkedState = remember { mutableStateOf(true) }

            Checkbox(
                checked = checkedState.value,
                onCheckedChange = { checkedState.value = it }
            )
        }
    }
}