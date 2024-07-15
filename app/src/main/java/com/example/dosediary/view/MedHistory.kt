package com.example.dosediary.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.dosediary.model.MedicationHistory
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dosediary.ui.theme.Background
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.dosediary.R
import com.example.dosediary.components.CustomTopAppBar
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@Composable
fun MedicationHistory(navController: NavHostController) {
    val context = LocalContext.current

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {generatePDF(context, sampleMedications)  },
                containerColor = Color(0xFF7DCBFF)
            ) {
                Icon(Icons.Filled.BarChart, contentDescription = "Generate PDF")
            }
        },
        topBar = {
            CustomTopAppBar(
                header = "Medication History",
                showNavigationIcon = false,
                navController = navController,
                imageResId = R.drawable.icon,  // Customizable icon
                imageDescription = "App Icon"
            )
        }
    ){ innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            items(sampleMedications) { medication ->
                MedicationItem(medication, navController)
            }
        }
    }
}

fun generatePDF(context: Context, medications: List<MedicationHistory>) {
    val pdfDocument = PdfDocument()
    val pageInfo = PdfDocument.PageInfo.Builder(300, 600, 1).create()
    val page = pdfDocument.startPage(pageInfo)

    val canvas = page.canvas

    // Create a Paint object
    val paint = Paint().apply {
        textSize = 12f
        isAntiAlias = true
    }

    var yPosition = 10

    medications.forEach { medication ->
        canvas.drawText("Name: ${medication.name}", 10f, yPosition.toFloat(), paint)
        yPosition += 20
        canvas.drawText("Time: ${medication.timeTaken}", 10f, yPosition.toFloat(), paint)
        yPosition += 20
        canvas.drawText("Date: ${medication.dateTaken}", 10f, yPosition.toFloat(), paint)
        yPosition += 20
        canvas.drawText("Effectiveness: ${medication.effectiveness}", 10f, yPosition.toFloat(), paint)
        yPosition += 30
    }

    pdfDocument.finishPage(page)

    val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "MedicationHistory.pdf")
    try {
        pdfDocument.writeTo(FileOutputStream(file))
        Log.d("PDF Generation", "PDF file generated successfully at ${file.absolutePath}")
    } catch (e: IOException) {
        e.printStackTrace()
        Log.e("PDF Generation", "Error generating PDF: ${e.message}")
    } finally {
        pdfDocument.close()
    }
}


val sampleMedications = listOf(
    MedicationHistory(0,"Ibuprofen", "Wednesday 6:00 PM", "2024/06/26", "Effective"),
    MedicationHistory(1,"Amoxicillin", "Wednesday 7:30 PM", "2024/06/26", "Effective"),
    MedicationHistory(2,"Amoxicillin", "Wednesday 7:30 PM","2024/06/26", "Effective"),
    MedicationHistory(3,"Amoxicillin", "Wednesday 7:30 PM", "2024/06/26","Effective"),
    MedicationHistory(4,"Amoxicillin", "Wednesday 7:30 PM", "2024/06/26","Effective"),
    MedicationHistory(5,"Amoxicillin", "Wednesday 7:30 PM","2024/06/26", "Effective"),
    MedicationHistory(6,"Oxaprozin", "Thursday 5:00 AM","2024/06/27", "Effective")
)

@Composable
fun MedicationItem(medication: MedicationHistory, navController: NavHostController) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Background,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { navController.navigate("editMedication")},
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row (
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                Column(
                    modifier = Modifier.weight(1f)  // This makes the column take up all space except for the button
                ) {
                    Text(text = medication.name,
                        style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, fontSize = 12.sp))
                    Text(text = medication.timeTaken,
                        style = LocalTextStyle.current.copy( fontSize = 10.sp, fontStyle = FontStyle.Italic))
                    Text(text = medication.dateTaken,
                        style = LocalTextStyle.current.copy( fontSize = 10.sp, fontStyle = FontStyle.Italic))
                }
                Column (
                    modifier = Modifier
                        .weight(1f),
                    horizontalAlignment = Alignment.End
                ){
                    Text(text = medication.effectiveness,
                        style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, fontSize = 12.sp))

                }
            }
        }
    }
}

@Preview(showBackground =true, name = "MedHistory Preview")
@Composable
fun MedHistoryPreview(){
    val navController = rememberNavController()
    MedicationHistory(navController = navController);
}




