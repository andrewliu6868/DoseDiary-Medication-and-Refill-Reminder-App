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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SignUpScreen(){
    Surface(
        color = Color.White,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(28.dp),
    ){
        Column {
            /*LoginTitle(value = "Sign Page")
            Spacer(modifier = Modifier.height(12.dp))
            InputTextFields(labelValue = "First Name")
            Spacer(modifier = Modifier.height(12.dp))
            InputTextFields(labelValue = "Last Name")
            Spacer(modifier = Modifier.height(12.dp))
            InputTextFields(labelValue = "Email")
            Spacer(modifier = Modifier.height(12.dp))
            InputTextFields(labelValue = "Password")
            Spacer(modifier = Modifier.height(50.dp))*/
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpAttempt(onLogin: (String, String) -> Unit){
    val email = remember { mutableStateOf("") }
    val password = remember{ mutableStateOf("") }
    val firstName = remember{mutableStateOf("")}
    val lastName = remember { mutableStateOf("") }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(50.dp),
        color = Color.White
    ) {
        Column {
            LoginTitle(value = "Sign Up Page")
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(4.dp)),
                label = {Text(text = "First Name")},
                value = firstName.value,
                onValueChange = {newText->
                    firstName.value = newText
                },
                keyboardOptions = KeyboardOptions.Default,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Blue,
                    cursorColor = Color.Blue
                )
            )
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(4.dp)),
                label = {Text(text = "Last Name")},
                value = lastName.value,
                onValueChange = {newText->
                    lastName.value = newText
                },
                keyboardOptions = KeyboardOptions.Default,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Blue,
                    cursorColor = Color.Blue
                )
            )
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(4.dp)),
                label = {Text(text = "Email")},
                value = email.value,
                onValueChange = {newText->
                    email.value = newText
                },
                keyboardOptions = KeyboardOptions.Default,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Blue,
                    cursorColor = Color.Blue
                )
            )
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(4.dp)),
                label = {Text(text = "Password")},
                value = password.value,
                onValueChange = {newText->
                    password.value = newText
                },
                keyboardOptions = KeyboardOptions.Default,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Blue,
                    cursorColor = Color.Blue
                )
            )
            Spacer(modifier = Modifier.height(80.dp))
            Button(
                onClick = {onLogin(email.value,password.value)},
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(48.dp),
                contentPadding = PaddingValues(),
                colors = ButtonDefaults.buttonColors(Color.Transparent)){
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
                    Text(text="Register",
                        fontSize=18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

    }

}