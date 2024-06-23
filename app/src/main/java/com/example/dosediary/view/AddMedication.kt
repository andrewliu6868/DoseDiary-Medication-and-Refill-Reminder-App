package com.example.dosediary.view

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.DoseDiary.R


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun AddMedicationMain(){
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text(text = "Add Medication") })
        }
    ) {
        Column (modifier = Modifier
            .padding(horizontal = 8.dp)
            .statusBarsPadding()
        ){
            Text(text = "Medication Name", fontSize = 16.sp)
            Spacer(modifier = Modifier.height(16.dp))
            AddName()
            Spacer(modifier = Modifier.height(16.dp))
            Text("Start/End Dates")
            Spacer(modifier = Modifier.height(16.dp))
            AddMedDuration()
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Intake Frequency")
            Spacer(modifier = Modifier.height(16.dp))
            AddMedFrequency()
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Refill Days")
            Spacer(modifier = Modifier.height(16.dp))
            AddRefillDays()
            Spacer(modifier = Modifier.height(16.dp))
            SaveDeleteRow()
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownMenu(
    selectedValue: String,
    optionItems: List<String>,
    label: String,
    onValueChangedEvent: (String) -> Unit,
    modifier: Modifier = Modifier
){
    var expanded by remember {mutableStateOf(false)}

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = {expanded = !expanded},
        modifier = modifier
        ) {
            OutlinedTextField(readOnly = true,
                value = selectedValue,
                onValueChange = {},
                label={ Text(text = label)},
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                colors = OutlinedTextFieldDefaults.colors(),
                modifier = Modifier
                    .menuAnchor()
                )
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    optionItems.forEach {item: String ->
                        DropdownMenuItem(text = { Text(text = item) },
                            onClick = {
                                expanded = false
                                onValueChangedEvent(item)
                            }
                        )
                    }
                }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddName(){
    var nameState by remember { mutableStateOf("")}
 Box {
        OutlinedTextField(value = nameState, onValueChange = {nameState = it}, label= {Text("Name")} )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMedDuration(){
    var startDateState by remember { mutableStateOf("") }
    var endDateState by remember { mutableStateOf("") }
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
    var freqNum by remember {
        mutableStateOf("0")
    }
    var freqRate by remember {
        mutableStateOf("Hourly")
    }
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DropDownMenu(selectedValue = freqNum,
                optionItems = frequencyNum,
                label = "Times",
                onValueChangedEvent = {freqNum = it},
                modifier = Modifier.weight(1f))
            Text(text = "Per", modifier = Modifier.weight(1f))
            DropDownMenu(selectedValue = freqRate,
                optionItems = frequncyRate,
                label = "Frequency",
                onValueChangedEvent = {freqRate = it},
                modifier = Modifier.weight(1f))
        }
}

/*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMedFrequency(){
    val frequencyNum = remember { mutableListOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9") }
    val frequncyRate = remember{ mutableListOf("Hourly", "Daily", "Weekly", "Monthly", "Yearly") }
    var freqNum by remember {
        mutableStateOf("0")
    }
    var freqRate by remember {
        mutableStateOf("Hourly")
    }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DropDownMenu(selectedValue = freqNum,
                optionItems = frequencyNum,
                label = "Times",
                onValueChangedEvent = {freqNum = it},
                modifier = Modifier.weight(1f))
            Text(text = "Per", modifier = Modifier.weight(1f))
            DropDownMenu(selectedValue = freqRate,
                optionItems = frequncyRate,
                label = "Frequency",
                onValueChangedEvent = {freqRate = it},
                modifier = Modifier.weight(1f))
        }
}
* */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRefillDays(){
    var sliderPosition by remember {mutableFloatStateOf(0f)}
    Box(modifier = Modifier) {
        Slider(
            value = sliderPosition,
            onValueChange = {sliderPosition = it},
            steps = 1,
            valueRange=0f..100f
        )
    }
    Text(text = sliderPosition.toString(), )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaveDeleteRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ){
        Button(
            onClick = {null},
            modifier = Modifier.weight(1f)
        ){
            Text("Save")
        }
        Spacer(modifier = Modifier.width(16.dp))
        Button(
            onClick = { null },
            modifier = Modifier.weight(1f)
        ) {
            Text("Delete")
        }
    }
}

