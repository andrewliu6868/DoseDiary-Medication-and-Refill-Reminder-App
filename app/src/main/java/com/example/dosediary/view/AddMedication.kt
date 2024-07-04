package com.example.dosediary.view

import android.os.Build
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
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
import com.example.dosediary.ui.theme.Background
import com.example.dosediary.ui.theme.ContainerBackground
import com.example.dosediary.ui.theme.Primary
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddMedicationPage(navController: NavHostController){
    val medicationName = remember { mutableStateOf("") }
    val startDate = remember { mutableStateOf("") }
    val endDate = remember { mutableStateOf("") }
//    var daysUntilRefill by remember { mutableIntStateOf(50) }
//    val sideEffects = listOf("Nausea", "Vomiting", "Diarrhea", "Constipation", "Drowsiness", "Dizziness", "Headache", "Fatigue", "Dry Mouth", "Rash", "Insomnia")
//    val sideEffectsState = remember { sideEffects.map { it to mutableStateOf(false) }.toMap() }
//    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

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
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
        ){
            MedicationNameSection(medicationName)
            Spacer(modifier = Modifier.height(20.dp))
            MedDurationSection(startDate, endDate)
            Spacer(modifier = Modifier.height(20.dp))
            MedFrequencySection()
            Spacer(modifier = Modifier.height(20.dp))
            RefillDaysSection()
            Spacer(modifier = Modifier.height(20.dp))
            SaveDeleteRow(navController)
        }
    }
}

@Composable
fun MedicationNameSection(medicationName: MutableState<String>){
    Text(text = "Medication Name",
        style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, fontSize = 17.sp))
    Spacer(modifier = Modifier.height(5.dp))
    OutlinedTextField(
        value = medicationName.value,
        onValueChange = { medicationName.value = it },
        label = { Text("Name") },
        modifier = Modifier.fillMaxWidth()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedDurationSection(startDate: MutableState<String>, endDate: MutableState<String>){
    Text(text = "Medication Period",
        style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, fontSize = 17.sp))
    Spacer(modifier = Modifier.height(5.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ){
        OutlinedTextField(value = startDate.value,
            onValueChange = {startDate.value = it},
            label = { Text("Start Date") },
            modifier = Modifier.weight(1f)
        )
        OutlinedTextField(value = endDate.value,
            onValueChange = { endDate.value = it },
            label = { Text("End Date") },
            modifier = Modifier.weight(1f)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedFrequencySection(){
    val frequencyNum = remember { mutableListOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9") }
    val frequncyRate = remember{ mutableListOf("Hourly", "Daily", "Weekly", "Monthly", "Yearly") }
//    var freqNum by remember { mutableStateOf(frequencyNum[0]) }
//    var freqRate by remember { mutableStateOf(frequncyRate[0]) }
    Text(text = "Intake Frequency",
        style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, fontSize = 17.sp))
    Spacer(modifier = Modifier.height(5.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        DropDownMenu(
            //selectedChoice = frequencyNum[0],
            optionItems = frequencyNum,
            label = "Times")
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Per",
            style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, fontSize = 17.sp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        DropDownMenu(
            //selectedChoice = freqRate,
            optionItems = frequncyRate,
            label = "Rate"
        )
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
    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = {isExpanded = !isExpanded},
        modifier = Modifier.width(150.dp),
    ) {
        OutlinedTextField(
            modifier = Modifier.menuAnchor(),
            label = {Text(label)},
            //value = selectedChoice.value,
            value = selected,
            onValueChange = {},
            readOnly = true,
            trailingIcon = {ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Primary,
                unfocusedIndicatorColor = Color.DarkGray,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                disabledTextColor = Color.Black,
            )
        )
        ExposedDropdownMenu(expanded = isExpanded, onDismissRequest = { isExpanded = false}, modifier = Modifier.background(Color.White)) {
            optionItems.forEachIndexed { index, text ->
                DropdownMenuItem(
                    modifier = Modifier.background(Color.White),
                    text = { Text(text = text, color = Color.Black) },
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RefillDaysSection(){
    var sliderPosition by remember { mutableStateOf(50 ) }
    Text(text = "Refill Days", style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, fontSize = 17.sp))
    Spacer(modifier = Modifier.height(8.dp))
    Slider(
        value = sliderPosition.toFloat(),
        onValueChange = {newValue -> sliderPosition = newValue.toInt()},
        valueRange=0f..100f,
        steps = 100,
        colors = SliderDefaults.colors(
            thumbColor = Primary,               // Color of the thumb (the draggable circle)
            activeTrackColor = Primary,         // Color of the active track (left of the thumb)
//            inactiveTrackColor =
        )
    )
    Text(modifier = Modifier.fillMaxWidth(), text = sliderPosition.toString(), textAlign = TextAlign.Center )
}

@Composable
fun SaveDeleteRow(navController: NavHostController) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ){
        Button(
            onClick = {navController.navigate("home")},
            colors = ButtonDefaults.buttonColors(containerColor = Primary),
            modifier = Modifier.weight(1f),
            elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 5.dp)
        ){
            Text("Save")
        }
        Spacer(modifier = Modifier.width(16.dp))
        Button(
            onClick = { navController.navigate("home") },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF7676)),
            modifier = Modifier.weight(1f),
            elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 5.dp)
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

