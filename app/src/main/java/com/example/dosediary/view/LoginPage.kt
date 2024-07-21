package com.example.dosediary.view

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardDefaults.shape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.dosediary.R
import com.example.dosediary.state.LoginState
import com.example.dosediary.ui.theme.ContainerBackground
import com.example.dosediary.ui.theme.Primary
import com.example.dosediary.viewmodel.LoginViewModel

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun LoginPage(navHostController: NavHostController){
    val viewModel = hiltViewModel<LoginViewModel>()
    val loginState by viewModel.loginState.collectAsState()

    when(loginState){
        is LoginState.Idle ->{
            LoginAttempt( {email, password ->
                    viewModel.login(email, password) },
                    navHostController)
        }

        is LoginState.Loading -> {
            LoginLoading()
        }

        is LoginState.Success -> {
            navHostController.navigate("home")

            }

        is LoginState.Error -> {
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                fontWeight = FontWeight.Bold,
                color = Color.Red,
                text="Error: ${loginState.error}")
            LoginAttempt({email, password ->
                viewModel.login(email, password)},
                navHostController)
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginAttempt(onLogin: (String, String) -> Unit, navHostController: NavHostController){
    val email = remember { mutableStateOf("") }
    val password = remember{ mutableStateOf("") }

    var showPassword by remember { mutableStateOf(value = false) }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(50.dp),
        color = Color.White
    ) {
        Column {
            LoginIcon()
            Spacer(modifier = Modifier.height(40.dp))
            Card(
                shape = RoundedCornerShape(35.dp),
                colors = CardDefaults.cardColors(containerColor = ContainerBackground),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            ){
                Column(modifier = Modifier.padding(20.dp)) {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(4.dp),
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
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(4.dp),
                        label = {Text(text = "Password")},
                        value = password.value,
                        onValueChange = {newText->
                            password.value = newText
                        },
                        visualTransformation = if (showPassword) {

                            VisualTransformation.None

                        } else {

                            PasswordVisualTransformation()

                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        trailingIcon = {
                            if (showPassword) {
                                IconButton(onClick = { showPassword = false }) {
                                    Icon(
                                        imageVector = Icons.Filled.Visibility,
                                        contentDescription = "hide_password"
                                    )
                                }
                            }else{
                                IconButton(onClick = { showPassword = true}) {
                                    Icon(
                                        imageVector = Icons.Filled.VisibilityOff,
                                        contentDescription = "show_password"
                                    )

                                }

                            }

                        },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color.Blue,
                            cursorColor = Color.Blue
                        )
                    )

                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {onLogin(email.value,password.value)},
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(48.dp),
                contentPadding = PaddingValues(),
                colors = ButtonDefaults.buttonColors(containerColor = Primary)
            ){
                Box (
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(48.dp)
                        .background(
                            color = Primary,
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
            Spacer(modifier = Modifier.height(5.dp))
            ClickableText(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                text = AnnotatedString(
                    "Sign Up Here",
                ),
                style = TextStyle(
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline,
                    color = Color.Blue
                ),
                onClick = {navHostController.navigate("signup")})


        }

    }

}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun LoginIcon(){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(40.dp),
        contentAlignment = Alignment.Center
    ){
        Image(painter = painterResource(id = R.drawable.icon),
            contentDescription = "App Main Icon",
            modifier = Modifier.size(100.dp))
    }
}

@Composable
fun LoginLoading() {
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



