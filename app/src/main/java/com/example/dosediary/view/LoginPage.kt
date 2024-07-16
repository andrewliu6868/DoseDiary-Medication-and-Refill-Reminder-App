package com.example.dosediary.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.pm.ShortcutInfoCompat.Surface

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun LoginScreen(){
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column {
            LoginTitle(value = "Login Page")
            Spacer(modifier = Modifier.height(8.dp))
            InputTextFields(labelValue = "Email")
            Spacer(modifier = Modifier.height(8.dp))
            InputPasswordFields(labelValue = "Password")
            Spacer(modifier = Modifier.height(8.dp))
            SubmitButton(value = "Login")
        }

    }
}


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun LoginTitle(value: String){
    Text(
        text = value,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 80.dp),
        style = TextStyle(
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
            //fontStyle = FontStyle.Normal
        ),
        textAlign = TextAlign.Center
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun InputTextFields(labelValue: String){
    var textValue by remember{
        mutableStateOf("")
    }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp)),
        label = {Text(text = labelValue)},
        value = textValue,
        onValueChange = {textValue = it},
        keyboardOptions = KeyboardOptions.Default,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Blue,
            cursorColor = Color.Blue
        )
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun InputPasswordFields(labelValue: String){
    var textValue by remember{
        mutableStateOf("")
    }

    var passwordVisible by remember{
        mutableStateOf(false)
    }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp)),
        label = {Text(text = labelValue)},
        value = textValue,
        onValueChange = {textValue = it},
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Blue,
            cursorColor = Color.Blue
        )
    )
}

@Composable
fun SubmitButton(value: String){
   Button(
       onClick = { /*TODO*/ },
       modifier = Modifier
           .fillMaxWidth()
           .heightIn(48.dp),
       contentPadding = PaddingValues(),
       colors = ButtonDefaults.buttonColors(Color.Transparent)
   ) {
        Box (
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(48.dp)
                .background(
                    brush = Brush.horizontalGradient(listOf(Color.Blue, Color.Cyan)),
                    shape = RoundedCornerShape(50.dp)
                ),
                contentAlignment = Alignment.Center
        ){
            Text(text=value,
                fontSize=18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
@Preview
@Composable
fun LoginPreview(){
    LoginScreen()
}