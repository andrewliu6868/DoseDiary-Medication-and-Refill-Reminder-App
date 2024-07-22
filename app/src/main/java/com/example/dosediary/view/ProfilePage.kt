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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dosediary.R
import com.example.dosediary.components.CustomTopAppBar
import com.example.dosediary.event.ProfileEvent
import com.example.dosediary.model.entity.User
import com.example.dosediary.state.ProfileState
import com.example.dosediary.ui.theme.ContainerBackground
import com.example.dosediary.ui.theme.OutlineTextField
import com.example.dosediary.ui.theme.Primary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilePage(
    navController: NavController,
    state: ProfileState,
    onEvent: (ProfileEvent) -> Unit
) {
    var isAddingUser by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CustomTopAppBar(
                header = stringResource(R.string.profile, state.currentUser?.firstName ?: "", state.currentUser?.lastname ?: ""),
                showNavigationIcon = false,
                navController = navController,
                imageResId = R.drawable.icon,  // Customizable icon
                imageDescription = stringResource(R.string.app_icon)
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
                                text = stringResource(R.string.add_user),
                                style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, fontSize = 20.sp),
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = state.addUserFirstName,
                                onValueChange = { newText ->
                                    onEvent(ProfileEvent.onAddUserFirstNameChanged(newText))
                                },
                                label = { Text(stringResource(R.string.first_name)) },
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
                                    onEvent(ProfileEvent.onAddUserLastNameChanged(newText))
                                },
                                label = { Text(stringResource(R.string.last_name)) },
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
                                    Text(stringResource(R.string.cancel))
                                }

                                Button(
                                    onClick = {
                                        onEvent(ProfileEvent.addUser)
                                        isAddingUser = false
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Primary),
                                    modifier = Modifier.align(Alignment.CenterVertically)
                                ) {
                                    Text(stringResource(R.string.add))
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
                        onEvent = onEvent
                    )
                }
                item { UserDetail(state, onEvent) }
//                item { MedicationHistory(navController) }
//                item { MedicationDetail(navController)}
            }
            if (state.showDeleteConfirmationDialog) {
                UserDeleteConfirmationDialog(
                    onConfirm = {
                        onEvent(ProfileEvent.confirmDeleteCurrentUser)
                    },
                    onDismiss = {
                        onEvent(ProfileEvent.cancelDeleteCurrentUser)
                    }
                )
            }
        }

    }
}

@Composable
fun ManageUsers(
    navController: NavController,
    state: ProfileState,
    onItemClick: () -> Unit,
    onEvent: (ProfileEvent) -> Unit
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
                text = stringResource(R.string.manage_users),
                style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, fontSize = 20.sp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            LazyRow {
                items( state.managedUsers ) { user ->
                    Users(
                        user = user,
                        onItemClick = {
                            onEvent(ProfileEvent.onChangeUser(user))
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
            text = stringResource(R.string.add_users),
            style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, fontSize = 10.sp)
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetail(
    state: ProfileState,
    onEvent: (ProfileEvent) -> Unit
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
                text = stringResource(R.string.user_details),
                style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, fontSize = 20.sp)
            )
            Spacer(modifier = Modifier.height(8.dp))


            OutlinedTextField(
                value = state.editCurrentUserFirstName,
                onValueChange = {
                    onEvent(ProfileEvent.onCurrentUserFirstNameChanged(it))
                },
                label = { Text(stringResource(R.string.first_name)) },
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
                value = state.editCurrentUserLastName,
                onValueChange = {
                    onEvent(ProfileEvent.onCurrentUserLastNameChanged(it))
                },
                label = { Text(stringResource(R.string.last_name)) },
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
            if ((state.currentUser?.id ?: 0) == (state.mainUser?.id ?: 0)) {
                OutlinedTextField(
                    value = state.editCurrentUserEmail,
                    onValueChange = {
                        onEvent(ProfileEvent.onCurrentUserEmailChanged(it))
                    },
                    label = { Text(stringResource(R.string.email)) },
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
                    value = state.editCurrentUserPassword,
                    onValueChange = {
                        onEvent(ProfileEvent.onCurrentUserPasswordChanged(it))
                    },
                    label = {
                        Text(text = stringResource(R.string.password))
                    },
                    placeholder = { Text(text = stringResource(R.string.type_password_here)) },
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
                                    contentDescription = stringResource(R.string.hide_password)
                                )
                            }
                        } else {
                            IconButton(
                                onClick = { showPassword = true }) {
                                Icon(
                                    imageVector = Icons.Filled.VisibilityOff,
                                    contentDescription = stringResource(R.string.show_password)
                                )
                            }
                        }
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))
            }
            if (state.editCurrentUserFirstName != state.currentUser?.firstName ||
                state.editCurrentUserLastName != state.currentUser?.lastname ||
                state.editCurrentUserEmail != state.currentUser?.email ||
                state.editCurrentUserPassword != state.currentUser?.password
            ) {

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            onEvent(ProfileEvent.cancelUpdateCurrentUser)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF7676)),
                        modifier = Modifier.align(Alignment.CenterVertically)
                    ) {
                        Text(stringResource(R.string.cancel))
                    }

                    Button(
                        onClick = {
                            onEvent(ProfileEvent.updateCurrentUser)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Primary),
                        modifier = Modifier.align(Alignment.CenterVertically)
                    ) {
                        Text(stringResource(R.string.save_changes))
                    }
                }
            }

            if (state.currentUser != state.mainUser) {
                Button(
                    onClick = {
                        onEvent(ProfileEvent.onDeleteCurrentUser)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF7676)),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(stringResource(R.string.delete_user))
                }
            }

        }
    }

}
@Composable
fun UserDeleteConfirmationDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(R.string.confirm_user_delete)) },
        text = { Text(text = stringResource(R.string.confirm_user_delete_message)) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(stringResource(R.string.yes))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.no))
            }
        }
    )
}

//@Composable
//fun MedicationHistory(navController: NavController) {
//    Card(
//        shape = RoundedCornerShape(35.dp),
//        colors = CardDefaults.cardColors(containerColor = ContainerBackground),
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 8.dp)
//    ) {
//        Column(modifier = Modifier.padding(16.dp)) {
//            BasicText(
//                text = "Medication History",
//                style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, fontSize = 20.sp)
//            )
//            Spacer(modifier = Modifier.height(8.dp))
//
//            Button(
//                onClick = { navController.navigate("history") },
//                colors = ButtonDefaults.buttonColors(containerColor = Primary),
//                modifier = Modifier.align(Alignment.CenterHorizontally)
//            ) {
//                Text("View Medication History")
//            }
//        }
//    }
//}

//@Composable
//fun MedicationDetail(navController: NavController) {
//    Card(
//        shape = RoundedCornerShape(35.dp),
//        colors = CardDefaults.cardColors(containerColor = ContainerBackground),
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 8.dp)
//    ) {
//        Column(modifier = Modifier.padding(16.dp)) {
//            BasicText(
//                text = "Medication Details",
//                style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, fontSize = 20.sp)
//            )
//            Spacer(modifier = Modifier.height(8.dp))
//
//            Button(
//                onClick = { navController.navigate("history") },
//                colors = ButtonDefaults.buttonColors(containerColor = Primary),
//                modifier = Modifier.align(Alignment.CenterHorizontally)
//            ) {
//                Text("View All Medication Details")
//            }
//        }
//    }
//
//}