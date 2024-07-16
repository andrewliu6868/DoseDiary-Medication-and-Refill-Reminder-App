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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.dosediary.viewmodel.SignUpState
import com.example.dosediary.viewmodel.SignUpViewModel
import kotlin.math.sign

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SignUpScreen(navHostController: NavHostController, viewModel:SignUpViewModel){
    val signUpState by viewModel.signUpState.collectAsState()
    when(signUpState){
        is SignUpState.Idle -> {
            SignUpAttempt{firstName, lastName, email, password ->
                viewModel.addUser(firstName, lastName, email,password)
            }
        }

        is SignUpState.Loading -> {
            SignUpLoading()
        }

        is SignUpState.Success ->{
            // go back to Login Screen
            viewModel.resetSignUpState()
            navHostController.navigate("login")
        }

        is SignUpState.Error ->{
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                fontWeight = FontWeight.Bold,
                color = Color.Red,
                text="Error: ${signUpState.error}")
            SignUpAttempt{firstName, lastName, email, password ->
                viewModel.addUser(firstName, lastName, email,password)
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpAttempt(onSignUp: (String, String, String, String) -> Unit){
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
            LoginTitle(value = "Sign Up")
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
            Spacer(modifier = Modifier.height(40.dp))
            Button(
                onClick = {onSignUp(firstName.value, lastName.value, email.value, password.value)},
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

@Composable
fun SignUpLoading() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        CircularProgressIndicator(
            modifier = Modifier.width(32.dp),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,

            )
    }
}