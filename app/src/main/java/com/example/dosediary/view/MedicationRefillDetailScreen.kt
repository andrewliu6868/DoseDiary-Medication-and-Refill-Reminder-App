package com.example.dosediary.view


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.DoseDiary.R
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.tooling.preview.Preview


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicationRefillDetailScreen(navController: NavController, medicationName: String?) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Medication Refill Info") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: Handle language change */ }) {
                        Icon(Icons.Filled.Info, contentDescription = "Language")
                    }
                    IconButton(onClick = { /* TODO: Handle calendar event */ }) {
                        Icon(Icons.Filled.DateRange, contentDescription = "Calendar")
                    }
                    IconButton(onClick = { /* TODO: Handle more options */ }) {
                        Icon(Icons.Filled.MoreVert, contentDescription = "More")
                    }
                }
            )
        },
        content = { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                item {
                    MedicationDetailContent(medicationName)
                }
            }
        }
    )
}

@Composable
fun MedicationDetailContent(medicationName: String?) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Card(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row() {
                    Image(
                        painter = painterResource(id = R.drawable.ic_medication),
                        contentDescription = null,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = medicationName ?: "Medication",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "2 Pills Left",
                            fontSize = 16.sp,
                            color = Color.Gray
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = { /* TODO: Handle more options */ }) {
                        Icon(Icons.Filled.MoreVert, contentDescription = "More")
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Image(
                    painter = painterResource(id = R.drawable.ic_profile),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Location:",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Shoppers Drug Mart (75 King St S Unit 42/43)",
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = { /* TODO: Handle edit click */ }
                    ) {
                        Text("Edit")
                    }
                    Button(
                        onClick = { /* TODO: Handle refill complete click */ }
                    ) {
                        Text("Refill Complete", color = Color.White)
                    }
                }
            }
        }
    }
}
@Preview
@Composable
fun MedicationRefillDetailScreenPreview() {
    // Preview the MedicationRefillDetailScreen
     val navController = rememberNavController()
    MedicationRefillDetailScreen(navController = navController, medicationName = "Medication Name")
}