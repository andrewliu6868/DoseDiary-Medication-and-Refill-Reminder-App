package com.example.dosediary.view

import android.content.Context
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.dosediary.R
import com.example.dosediary.components.CustomTopAppBar
import com.example.dosediary.events.MedicationHistoryEvent
import com.example.dosediary.model.entity.MedicationHistory
import com.example.dosediary.state.MedicationHistoryState
import com.example.dosediary.ui.theme.Background
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@Composable
fun MedicationHistoryPage(
    navController: NavHostController,
    state: MedicationHistoryState,
    onEvent: (MedicationHistoryEvent) -> Unit,
    addTestEntries: () -> Unit
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        addTestEntries()
    }

    Scaffold(
        topBar = {
            CustomTopAppBar(
                header = "Medication History",
                showNavigationIcon = true,
                navController = navController,
                imageResId = R.drawable.icon,
                imageDescription = "App Icon",
                onActionButtonClick = {
                    generatePDF(context, state.medicationHistories) { result ->
                        scope.launch {
                            if (result) {
                                snackbarHostState.showSnackbar("PDF generated successfully")
                            } else {
                                snackbarHostState.showSnackbar("Error generating PDF")
                            }
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    //navigate to add medication page?
                    navController.navigate("editMedication")
                },
                containerColor = Color(0xFF7DCBFF)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Medication")
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(state.medicationHistories) { medication ->
                MedicationItem(medication, navController)
            }
        }
    }
}

fun generatePDF(context: Context, medications: List<MedicationHistory>, onResult: (Boolean) -> Unit) {
    val pdfDocument = PdfDocument()
    val pageWidth = 300
    val pageHeight = 600
    val paint = Paint().apply {
        textSize = 12f
        isAntiAlias = true
    }

    val itemsPerPage = 6
    val totalPages = (medications.size + itemsPerPage - 1) / itemsPerPage

    for (pageIndex in 0 until totalPages) {
        val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageIndex + 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas

        var yPosition = 40f
        val startItemIndex = pageIndex * itemsPerPage
        val endItemIndex = minOf(startItemIndex + itemsPerPage, medications.size)

        for (i in startItemIndex until endItemIndex) {
            val medication = medications[i]
            canvas.drawText("Name: ${medication.name}", 10f, yPosition, paint)
            yPosition += 20
            canvas.drawText("Time: ${medication.timeTaken}", 10f, yPosition, paint)
            yPosition += 20
            canvas.drawText("Date: ${medication.dateTaken}", 10f, yPosition, paint)
            yPosition += 20
            canvas.drawText("Effectiveness: ${medication.effectiveness}", 10f, yPosition, paint)
            yPosition += 30

            // Check if the current page is full
            if (yPosition + 30 > pageHeight) {
                break
            }
        }

        pdfDocument.finishPage(page)
    }

    val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "MedicationHistory.pdf")
    try {
        pdfDocument.writeTo(FileOutputStream(file))
        Log.d("PDF Generation", "PDF file generated successfully at ${file.absolutePath}")
        onResult(true)
    } catch (e: IOException) {
        e.printStackTrace()
        Log.e("PDF Generation", "Error generating PDF: ${e.message}")
        onResult(false)
    } finally {
        pdfDocument.close()
    }
}

@Composable
fun MedicationItem(medication: MedicationHistory, navController: NavHostController) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Background,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { navController.navigate("editMedication") },
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = medication.name,
                        style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    )
                    Text(
                        text = medication.timeTaken,
                        style = LocalTextStyle.current.copy(fontSize = 10.sp, fontStyle = FontStyle.Italic)
                    )
                    Text(
                        text = medication.dateTaken,
                        style = LocalTextStyle.current.copy(fontSize = 10.sp, fontStyle = FontStyle.Italic)
                    )
                }
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = medication.effectiveness,
                        style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "MedHistory Preview")
@Composable
fun MedHistoryPreview() {
    val navController = rememberNavController()
    val state = MedicationHistoryState()
    val onEvent: (MedicationHistoryEvent) -> Unit = {}
    MedicationHistoryPage(
        navController = navController,
        state = state,
        onEvent = onEvent,
        addTestEntries = {}
    )
}
