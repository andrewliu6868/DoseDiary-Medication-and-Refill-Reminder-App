package com.example.dosediary.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.DoseDiary.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMedicationMain(){


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTopBar(){

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddName(){
    var nameState by remember { mutableStateOf("")}
    Column (modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
        Text(text = "Medication Name", fontSize = 16.sp)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = nameState, onValueChange = {nameState = it}, label= {Text("Name")} )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMedDuration(){
    var startDateState by remember { mutableStateOf("") }
    var endDateState by remember { mutableStateOf("") }
    Column(){
        Text("Start/End Dates")
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ){
            OutlinedTextField(value = startDateState, onValueChange = {startDateState = it}, label = {Text("Start Date")} )
            OutlinedTextField(value = endDateState, onValueChange = {endDateState = it}, label = {Text("End Date")} )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMedFrequency(){
    var frequencyNum by remember {
        mutableStateOf(0f)
    }
    var freqeuncyRate by remember{
        mutableStateOf("")
    }
    Column {
        Text(text = "Intake Frequency")
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = "Per")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRefillDays(){
    var sliderPosition by remember {mutableFloatStateOf(0f)}
    Column {
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
@Preview
@Composable
fun AddMedicationPreview(){
    AddMedicationMain()
}
