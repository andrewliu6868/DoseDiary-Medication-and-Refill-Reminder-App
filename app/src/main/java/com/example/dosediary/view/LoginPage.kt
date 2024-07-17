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
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.pm.ShortcutInfoCompat.Surface
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.dosediary.viewmodel.LoginState
import com.example.dosediary.viewmodel.LoginViewModel

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun LoginScreen(viewModel: LoginViewModel){
    val loginState by viewModel.loginState.collectAsState()
    val navController = rememberNavController()

    when(loginState){
        is LoginState.Idle ->{
            LoginAttempt {email, password ->
                viewModel.login(email, password)
            }
        }

        is LoginState.Loading -> {

        }

        is LoginState.Success -> {
            //Navigate to Home Screen
            navController.navigate("home")
            /*Text(
                modifier = Modifier
                    .fillMaxWidth(),
                fontWeight = FontWeight.Bold,
                text="Success")*/
            }

        is LoginState.Error -> {
                LoginAttempt {email, password ->
                    viewModel.login(email, password)
                }
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    fontWeight = FontWeight.Bold,
                    text="Error: ${loginState.error}")
                }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginAttempt(onLogin: (String, String) -> Unit){
    val email = remember { mutableStateOf("") }
    val password = remember{ mutableStateOf("") }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(50.dp),
        color = Color.White
    ) {
        Column {
            LoginTitle(value = "Login Page")
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
                    Text(text="Login",
                        fontSize=18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
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



