package com.example.dosediary.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeCompilerApi
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.dosediary.R
import com.example.dosediary.components.CustomTopAppBar
import com.example.dosediary.event.ProfileEvent
import com.example.dosediary.model.entity.User
import com.example.dosediary.state.ProfileState
import com.example.dosediary.ui.theme.Background
import com.example.dosediary.ui.theme.ContainerBackground
import com.example.dosediary.ui.theme.OutlineTextField
import com.example.dosediary.ui.theme.Primary
import com.example.dosediary.viewmodel.MedRefillDetailViewModel
import com.example.dosediary.viewmodel.MedRefillViewModel
import com.example.dosediary.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Profile(
    navController: NavController
) {
    val profileViewModel = hiltViewModel<ProfileViewModel>()
    val state by profileViewModel.state.collectAsState()

    var isAddingUser by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CustomTopAppBar(
                header = "Profile: ${state.currentUser?.firstName} ${state.currentUser?.lastname}",
                showNavigationIcon = false,
                navController = navController,
                imageResId = R.drawable.icon,  // Customizable icon
                imageDescription = "App Icon"
            )
        }
    ) { padding ->

        if (isAddingUser) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(padding)
                    .padding(horizontal = 16.dp)
            ) {
                item {
                    Card(
                        shape = RoundedCornerShape(35.dp),
                        colors = CardDefaults.cardColors(containerColor = ContainerBackground),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            BasicText(
                                text = "Add User",
                                style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, fontSize = 20.sp),
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = state.addUserFirstName,
                                onValueChange = { newText ->
                                    profileViewModel.onEvent(ProfileEvent.onAddUserFirstNameChanged(newText))
                                },
                                label = { Text("First Name") },
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    unfocusedTextColor = Color.Black,
                                    focusedBorderColor = OutlineTextField, // Custom focused border color
                                    unfocusedBorderColor = OutlineTextField // Custom unfocused border color
                                )
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = state.addUserLastName,
                                onValueChange = {newText ->
                                    profileViewModel.onEvent(ProfileEvent.onAddUserLastNameChanged(newText))
                                },
                                label = { Text("Last Name") },
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    unfocusedTextColor = Color.Black,
                                    focusedBorderColor = OutlineTextField, // Custom focused border color
                                    unfocusedBorderColor = OutlineTextField // Custom unfocused border color
                                )
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            //add cancel buttom and add buttom
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Button(
                                    onClick = { isAddingUser = false },
                                    colors = ButtonDefaults.buttonColors(containerColor = Primary),
                                    modifier = Modifier.align(Alignment.CenterVertically)
                                ) {
                                    Text("Cancel")
                                }

                                Button(
                                    onClick = {
                                        profileViewModel.onEvent(ProfileEvent.addUser)
                                        isAddingUser = false
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Primary),
                                    modifier = Modifier.align(Alignment.CenterVertically)
                                ) {
                                    Text("Add")
                                }

                            }
                        }
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(padding)
                    .padding(horizontal = 16.dp)
            ) {
                item {
                    ManageUsers(
                        navController = navController,
                        state = state,
                        onItemClick = {
                            isAddingUser = true
                        },
                        profileViewModel = profileViewModel
                    )
                }
                item { UserDetail(state, profileViewModel) }
                item { MedicationHistory(navController) }
                item { MedicationDetail(navController)}
            }
        }

    }
}

@Composable
fun ManageUsers(
    navController: NavController,
    state: ProfileState,
    onItemClick: () -> Unit,
    profileViewModel: ProfileViewModel
) {

    Card(
        shape = RoundedCornerShape(35.dp),
        colors = CardDefaults.cardColors(containerColor = ContainerBackground),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            BasicText(
                text = "Manage Users",
                style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, fontSize = 20.sp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            LazyRow {
                items( state.managedUsers ) { user ->
                    Users(
                        user = user,
                        onItemClick = {
                            profileViewModel.onEvent(ProfileEvent.onChangeUser(user))
                        }
                    )
                }
                item { AddUsers(
                    onItemClick = {
                        onItemClick()
                    }
                ) }
            }

        }
    }
}



@Composable
fun Users(user: User, onItemClick: () -> Unit = {}) {
    Column (
        modifier = Modifier
            .padding(16.dp)
            .clickable {
                onItemClick()
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ){
        Text(
            modifier = Modifier
                .padding(16.dp)
                .drawBehind {
                    drawCircle(
                        color = Color.Gray,
                        radius = 80.0f
                    )
                },
            text = user.firstName[0].toString() + user.lastname[0].toString(),
            style = TextStyle(color = Color.White, fontSize = 20.sp)
        )
        Spacer(modifier = Modifier.height(1.dp))
        BasicText(
            text = user.firstName + " " + user.lastname,
            style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, fontSize = 10.sp)
        )

    }
}

@Composable
fun AddUsers(onItemClick: () -> Unit = {}) {
    Column (
        modifier = Modifier
            .padding(16.dp)
            .clickable {
                onItemClick()
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ){
        Text(
            modifier = Modifier
                .padding(16.dp)
                .drawBehind {
                    drawCircle(
                        color = Color.Gray,
                        radius = 80.0f
                    )
                },
            text = "+",
            style = TextStyle(color = Color.White, fontSize = 20.sp)
        )
        Spacer(modifier = Modifier.height(1.dp))
        BasicText(
            text = "Add Users",
            style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, fontSize = 10.sp)
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetail(
    state: ProfileState,
    profileViewModel: ProfileViewModel
) {

    var showPassword by remember { mutableStateOf(value = false) }

    Card(
        shape = RoundedCornerShape(35.dp),
        colors = CardDefaults.cardColors(containerColor = ContainerBackground),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {

        Column(modifier = Modifier.padding(16.dp)) {
            BasicText(
                text = "User Details",
                style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, fontSize = 20.sp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            if ((state.currentUser?.id ?: 0) == (state.mainUser?.id ?: 0)) {
                OutlinedTextField(
                    value = state.editMainUserFirstName,
                    onValueChange = {
                        profileViewModel.onEvent(ProfileEvent.onMainUserFirstNameChanged(it))
                    },
                    label = { Text("First Name") },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        unfocusedTextColor = Color.Black,
                        focusedBorderColor = OutlineTextField, // Custom focused border color
                        unfocusedBorderColor = OutlineTextField // Custom unfocused border color
                    ),
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(percent = 20),

                    )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = state.editMainUserLastName,
                    onValueChange = {
                        profileViewModel.onEvent(ProfileEvent.onMainUserLastNameChanged(it))
                    },
                    label = { Text("Last Name") },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        unfocusedTextColor = Color.Black,
                        focusedBorderColor = OutlineTextField, // Custom focused border color
                        unfocusedBorderColor = OutlineTextField // Custom unfocused border color
                    ),
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(percent = 20),
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = state.editMainUserEmail,
                    onValueChange = {
                        profileViewModel.onEvent(ProfileEvent.onMainUserEmailChanged(it))
                    },
                    label = { Text("Email") },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        unfocusedTextColor = Color.Black,
                        focusedBorderColor = OutlineTextField, // Custom focused border color
                        unfocusedBorderColor = OutlineTextField // Custom unfocused border color
                    ),
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(percent = 20),
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        unfocusedTextColor = Color.Black,
                        focusedBorderColor = OutlineTextField, // Custom focused border color
                        unfocusedBorderColor = OutlineTextField // Custom unfocused border color
                    ),
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = state.editMainUserPassword,
                    onValueChange = {
                        profileViewModel.onEvent(ProfileEvent.onMainUserPasswordChanged(it))
                    },
                    label = {
                        Text(text = "Password")
                    },
                    placeholder = { Text(text = "Type password here") },
                    shape = RoundedCornerShape(percent = 20),
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
                        } else {
                            IconButton(
                                onClick = { showPassword = true }) {
                                Icon(
                                    imageVector = Icons.Filled.VisibilityOff,
                                    contentDescription = "hide_password"
                                )
                            }
                        }
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                if (state.editMainUserFirstName != state.mainUser?.firstName ||
                    state.editMainUserLastName != state.mainUser?.lastname ||
                    state.editMainUserEmail != state.mainUser?.email ||
                    state.editMainUserPassword != state.mainUser?.password) {

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = {
                                profileViewModel.onEvent(ProfileEvent.cancelUpdateMainUser)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Primary),
                            modifier = Modifier.align(Alignment.CenterVertically)
                        ) {
                            Text("Cancel")
                        }

                        Button(
                            onClick = {
                                profileViewModel.onEvent(ProfileEvent.updateMainUser)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Primary),
                            modifier = Modifier.align(Alignment.CenterVertically)
                        ) {
                            Text("Save Changes")
                        }
                    }
                }


            } else {
                //TODO
            }
        }
    }

}


@Composable
fun MedicationHistory(navController: NavController) {
    Card(
        shape = RoundedCornerShape(35.dp),
        colors = CardDefaults.cardColors(containerColor = ContainerBackground),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            BasicText(
                text = "Medication History",
                style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, fontSize = 20.sp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { navController.navigate("history") },
                colors = ButtonDefaults.buttonColors(containerColor = Primary),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("View Medication History")
            }
        }
    }
}

@Composable
fun MedicationDetail(navController: NavController) {
    Card(
        shape = RoundedCornerShape(35.dp),
        colors = CardDefaults.cardColors(containerColor = ContainerBackground),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            BasicText(
                text = "Medication Details",
                style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, fontSize = 20.sp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { navController.navigate("history") },
                colors = ButtonDefaults.buttonColors(containerColor = Primary),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("View All Medication Details")
            }
        }
    }

}