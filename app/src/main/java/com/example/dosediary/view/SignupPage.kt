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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.dosediary.R
import com.example.dosediary.state.SignUpState
import com.example.dosediary.ui.theme.ContainerBackground
import com.example.dosediary.ui.theme.Primary
import com.example.dosediary.viewmodel.SignUpViewModel

@Composable
@OptIn(ExperimentalMaterial3Api::class)

fun SignUpPage(navHostController: NavHostController){
    val viewModel = hiltViewModel<SignUpViewModel>()
    val signUpState by viewModel.signUpState.collectAsState()
    when(signUpState){
        is SignUpState.Idle -> {
            SignUpAttempt{firstName, lastName, email, password ->
                viewModel.addUser(firstName, lastName, email,password)
            }
        }

        is SignUpState.Loading -> {

        }

        is SignUpState.Success ->{
            // go back to Login Screen
            viewModel.resetSignUpState()
            navHostController.navigate("login")
        }

        is SignUpState.Error -> {
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                fontWeight = FontWeight.Bold,
                color = Color.Red,
                text = stringResource(R.string.error, signUpState.error))
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
        color = Color(0xf7f9ff)
    ) {
        Column {
            SignUpIcon()
            Spacer(modifier = Modifier.height(20.dp))

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
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(4.dp)),
                        label = { Text(text = stringResource(R.string.first_name)) },
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
                        label = { Text(text = stringResource(R.string.last_name)) },
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
                        label = { Text(text = stringResource(R.string.email)) },
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
                        label = { Text(text = stringResource(R.string.password)) },
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
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {onSignUp(firstName.value, lastName.value, email.value, password.value)},
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(48.dp),
                contentPadding = PaddingValues(),
                colors = ButtonDefaults.buttonColors(containerColor = Primary)){
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
                    Text(text = stringResource(R.string.register),
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
fun SignUpIcon(){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(40.dp),
        contentAlignment = Alignment.Center
    ){
        Image(painter = painterResource(id = R.drawable.icon),
            contentDescription = stringResource(R.string.app_main_icon),
            modifier = Modifier.size(100.dp))
    }
}
