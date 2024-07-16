package com.example.dosediary.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Build
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.dosediary.R
import com.example.dosediary.components.CustomTopAppBar
import com.example.dosediary.components.DatePicker
import com.example.dosediary.components.TimePicker
import com.example.dosediary.ui.theme.Background
import com.example.dosediary.ui.theme.ContainerBackground
import com.example.dosediary.ui.theme.Primary
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun AddMedicationPage(navController: NavHostController){
    val medicationName = remember { mutableStateOf("") }

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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding)
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item { MedicationNameSection(medicationName) }
            item { MedDurationSection() }
            item { MedFrequencySection() }
            item { RefillDaysSection() }
            item { NoteSection() }
            item { SaveDeleteRow(navController) }
        }
    }
}

@Composable
fun MedicationNameSection(medicationName: MutableState<String>){
    Text(text = "Medication Name", style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, fontSize = 17.sp))
    Spacer(modifier = Modifier.height(5.dp))
    OutlinedTextField(
        value = medicationName.value,
        onValueChange = { medicationName.value = it },
        label = { Text("Name") },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun MedDurationSection(){
    val startDate = remember { mutableStateOf(Date()) }
    val endDate = remember { mutableStateOf(Date()) }

    Text(text = "Medication Period", style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, fontSize = 17.sp))
    Spacer(modifier = Modifier.height(5.dp))
    DatePicker(date = startDate, "Start Date")
    Spacer(modifier = Modifier.height(5.dp))
    DatePicker(date = endDate, "End Date")
}

@Composable
fun MedFrequencySection(){
    var frequency by remember { mutableStateOf(TextFieldValue("1")) }
    val times = remember { mutableStateListOf<Date>(Date()) }
    val dateFormat = remember { SimpleDateFormat("MM/dd/yyyy hh:mm a", Locale.getDefault()) }


    Text(text = "Intake Frequency", style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, fontSize = 17.sp))
    Spacer(modifier = Modifier.height(5.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Column {
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "Number of times per day:", style = LocalTextStyle.current.copy(fontWeight = FontWeight.Medium, fontSize = 15.sp))
        }
        Spacer(modifier = Modifier.width(8.dp))
        OutlinedTextField(
            modifier = Modifier
                .width(80.dp)
                .onFocusChanged { focusState ->
                    if (focusState.isFocused) {
                        frequency = frequency.copy(selection = TextRange(0, frequency.text.length))
                    }
                },
            value = frequency,
            onValueChange = { value ->
                frequency = value
                val intVal = value.text.toIntOrNull()
                //Validation
                if (intVal != null && intVal > 0) {
                    while (times.size < intVal) {
                        times.add(Date())
                    }
                    while (times.size > intVal) {
                        times.removeAt(times.size - 1)
                    }
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }
    Spacer(modifier = Modifier.height(10.dp))
    times.forEachIndexed { index, time ->
        val timeState = remember { mutableStateOf(time) }
        TimePicker(time = timeState, placeholder = "Select Time ${index + 1}")
        times[index] = timeState.value
        Spacer(modifier = Modifier.height(10.dp))
    }
    Spacer(modifier = Modifier.height(5.dp))
    times.forEachIndexed { index, time ->
        Text(text = "Time ${index + 1}: ${dateFormat.format(time)}")
    }
}


@Composable
fun RefillDaysSection(){
    var sliderPosition by remember { mutableIntStateOf(50 ) }
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
fun NoteSection() {
    var note by remember { mutableStateOf("") }

    Text(text = "Notes", style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, fontSize = 17.sp))
    Spacer(modifier = Modifier.height(5.dp))

    OutlinedTextField(
        value = note,
        onValueChange = { note = it },
        label = { Text("Add a note") },
        modifier = Modifier.fillMaxWidth()
    )
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

@Preview(showBackground =true, name = "AddMedication Preview")
@Composable
fun AddMedPreview(){
    val navController = rememberNavController()
    AddMedicationPage(navController);
}

