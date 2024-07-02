package com.example.dosediary.view


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Directions
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dosediary.R
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.dosediary.ui.theme.ContainerBackground
import com.example.dosediary.ui.theme.Primary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicationRefillDetailScreen(navController: NavController){
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Medication Refill Detail") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigateUp()
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ){padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp)

        ) {
            BasicText(
                text = "Medication Refill Detail",
                style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, fontSize = 20.sp)
            )
            MedicationRefillDetail()
        }
    }

}

val medicationRefill = MedicationRefill(
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

@Composable
fun MedicationRefillDetail() {
    Card(
        shape = RoundedCornerShape(35.dp),
        colors = CardDefaults.cardColors(containerColor = ContainerBackground),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Spacer(modifier = Modifier.height(8.dp))
            BasicText(
                text = medicationRefill.medicationName + ": " + medicationRefill.currentStock + " pills left",
                style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, fontSize = 15.sp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Image(
                painter = painterResource(id = R.drawable.img),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .height(200.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row() {
                Button(onClick = { /*TODO*/ },
                    colors = ButtonDefaults.buttonColors(containerColor = Primary),
                    modifier = Modifier.height(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Directions,
                        tint = Color.White,
                        contentDescription = "Direction"
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Direction")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { /*TODO*/ },
                    colors = ButtonDefaults.buttonColors(containerColor = Primary),
                    modifier = Modifier.height(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Phone,
                        tint = Color.White,
                        contentDescription = "Call"
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Call")
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                BasicText(
                    text = "Refill Information:",
                    style = LocalTextStyle.current.copy(fontWeight = FontWeight.Medium, fontSize = 14.sp)
                )

                Text(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Medium, fontSize = 12.sp)) {
                            append("Next Refill Date: ")
                        }
                        withStyle(style = SpanStyle(fontSize = 12.sp)) {
                            append(medicationRefill.nextRefillDate)
                        }
                    }
                )
                Text(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Medium, fontSize = 12.sp)) {
                            append("Pharmacy Name: ")
                        }
                        withStyle(style = SpanStyle(fontSize = 12.sp)) {
                            append(medicationRefill.pharmacyName)
                        }
                    }
                )
                Text(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Medium, fontSize = 12.sp)) {
                            append("Pharmacy Contact: ")
                        }
                        withStyle(style = SpanStyle(fontSize = 12.sp)) {
                            append(medicationRefill.pharmacyContact)
                        }
                    }
                )

                Text(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Medium, fontSize = 12.sp)) {
                            append("Pharmacy Address:")
                        }
                        withStyle(style = SpanStyle(fontSize = 12.sp)) {
                            append(medicationRefill.pharmacyAddress)
                        }
                    }
                )

                Text(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Medium, fontSize = 12.sp)) {
                            append("Prescription Number: ")
                        }
                        withStyle(style = SpanStyle(fontSize = 12.sp)) {
                            append(medicationRefill.prescriptionNumber)
                        }
                    }
                )

                Text(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Medium, fontSize = 12.sp)) {
                            append("Prescribing Doctor: ")
                        }
                        withStyle(style = SpanStyle(fontSize = 12.sp)) {
                            append(medicationRefill.prescribingDoctor)
                        }
                    }
                )

                Text(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Medium, fontSize = 12.sp)) {
                            append("Doctor Contact: ")
                        }
                        withStyle(style = SpanStyle(fontSize = 12.sp)) {
                            append(medicationRefill.doctorContact)
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                BasicText(
                    text = "More information about the medication:",
                    style = LocalTextStyle.current.copy(
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    )
                )

                Text(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Medium, fontSize = 12.sp)) {
                            append("Dosage: ")
                        }
                        withStyle(style = SpanStyle(fontSize = 12.sp)) {
                            append(medicationRefill.dosage)
                        }
                    }
                )

                Text(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Medium, fontSize = 12.sp)) {
                            append("Quantity: ")
                        }
                        withStyle(style = SpanStyle(fontSize = 12.sp)) {
                            append(medicationRefill.quantity.toString())
                        }
                    }
                )

                Text(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Medium, fontSize = 12.sp)) {
                            append("Notes: ")
                        }
                        withStyle(style = SpanStyle(fontSize = 12.sp)) {
                            append(medicationRefill.notes.toString())
                        }
                    }
                )

            }

        }
    }
}

@Preview(showBackground =true, name = "Medication Refill Detail Preview")
@Composable
fun MedRefillDetailPreview(){
    val navController = rememberNavController()
    MedicationRefillDetailScreen(navController = navController);
}