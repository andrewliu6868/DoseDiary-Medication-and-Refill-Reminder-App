package com.example.dosediary.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.dosediary.R
import com.example.dosediary.components.CustomTopAppBar
import com.example.dosediary.ui.theme.Primary
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddMedicationPage(navController: NavHostController){
    var medicationName by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf(LocalDate.now()) }
    var endDate by remember { mutableStateOf(LocalDate.now().plusDays(30)) }
    var daysUntilRefill by remember { mutableIntStateOf(50) }
    val sideEffects = listOf("Nausea", "Vomiting", "Diarrhea", "Constipation", "Drowsiness", "Dizziness", "Headache", "Fatigue", "Dry Mouth", "Rash", "Insomnia")
    val sideEffectsState = remember { sideEffects.map { it to mutableStateOf(false) }.toMap() }
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    Scaffold(
        topBar = {
            CustomTopAppBar(
                header = "Add Medication",
                showNavigationIcon = false,
                navController = navController,
                imageResId = R.drawable.icon,  // Customizable icon
                imageDescription = "App Icon"
            )
        }
    ){ innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp)
        ){
            MedicationNameSection()
            Spacer(modifier = Modifier.height(16.dp))
            AddMedDuration()
            Spacer(modifier = Modifier.height(16.dp))
            AddMedFrequency()
            Spacer(modifier = Modifier.height(16.dp))
            AddRefillDays()
            Spacer(modifier = Modifier.height(16.dp))
            SaveDeleteRow(navController)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownMenu(
    //selectedChoice: MutableState<String>,
    optionItems: List<String>,
    label: String
){
    var isExpanded by remember { mutableStateOf(false) }
    var selected by remember{ mutableStateOf("") }
    Column(
        modifier = Modifier
            .padding(horizontal = 8.dp)
    ){
        ExposedDropdownMenuBox(
            expanded = isExpanded,
            onExpandedChange = {isExpanded = !isExpanded}) {
            TextField(
                modifier = Modifier.menuAnchor(),
                label = {Text(label)},
                //value = selectedChoice.value,
                value = selected,
                onValueChange = {},
                readOnly = true,
                trailingIcon = {ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) }
            )
            ExposedDropdownMenu(expanded = isExpanded, onDismissRequest = { isExpanded = false}) {
                optionItems.forEachIndexed { index, text ->
                    DropdownMenuItem(
                        text = {Text(text = text) },
                        onClick = {
                            selected = optionItems[index]
                            isExpanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                }
            }
            //Text("Selected option is: " + selected)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicationNameSection(){
    var nameState by remember { mutableStateOf("")}
    Text(text = "Medication Name",
        style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, fontSize = 20.sp))
    Spacer(modifier = Modifier.height(16.dp))
    Box {
        OutlinedTextField(value = nameState, onValueChange = {nameState = it}, label= {Text("Name")} )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMedDuration(){
    var startDateState by remember { mutableStateOf("") }
    var endDateState by remember { mutableStateOf("") }
    Text("Start/End Dates",
        style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, fontSize = 20.sp))
    Spacer(modifier = Modifier.height(16.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ){
        OutlinedTextField(value = startDateState,
            onValueChange = {startDateState = it},
            label = {Text("Start Date")},
            modifier = Modifier.weight(1f))
        OutlinedTextField(value = endDateState,
            onValueChange = {endDateState = it},
            label = {Text("End Date")},
            modifier = Modifier.weight(1f))
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMedFrequency(){
    val frequencyNum = remember { mutableListOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9") }
    val frequncyRate = remember{ mutableListOf("Hourly", "Daily", "Weekly", "Monthly", "Yearly") }
    var freqNum by remember { mutableStateOf(frequencyNum[0]) }
    var freqRate by remember { mutableStateOf(frequncyRate[0]) }
    Text(text = "Intake Frequency",
        style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, fontSize = 20.sp))
    Spacer(modifier = Modifier.height(16.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        //horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ){
            DropDownMenu(
                //selectedChoice = frequencyNum[0],
                optionItems = frequencyNum,
                label = "Times")
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "Per")
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            DropDownMenu(
                //selectedChoice = freqRate,
                optionItems = frequncyRate,
                label = "Rate")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRefillDays(){
    var sliderPosition by remember {mutableStateOf(0)}
    Text(text = "Refill Days",
        style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, fontSize = 20.sp))
    Spacer(modifier = Modifier.height(16.dp))
    Box(modifier = Modifier) {
        Slider(
            value = sliderPosition.toFloat(),
            onValueChange = {newValue ->
                sliderPosition = newValue.toInt()
                            },
            valueRange=0f..100f,
            steps = 100
        )
    }
    Text(text = sliderPosition.toString(), textAlign = TextAlign.Center )
}

@Composable
fun SaveDeleteRow(navController: NavHostController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ){
        Button(
            onClick = {navController.navigate("home")},
            colors = ButtonDefaults.buttonColors(containerColor = Primary),
            modifier = Modifier.weight(1f)
        ){
            Text("Save")
        }
        Spacer(modifier = Modifier.width(16.dp))
        Button(
            onClick = { navController.navigate("home") },
            colors = ButtonDefaults.buttonColors(containerColor = Primary),
            modifier = Modifier.weight(1f)
        ) {
            Text("Delete")
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground =true, name = "AddMedication Preview")
@Composable
fun AddMedPreview(){
    val navController = rememberNavController()
    AddMedicationPage(navController);
}

