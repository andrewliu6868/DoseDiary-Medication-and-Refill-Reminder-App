package com.example.dosediary.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dosediary.R
import com.example.dosediary.components.CustomTopAppBar
import com.example.dosediary.ui.theme.ContainerBackground
import com.example.dosediary.ui.theme.OutlineTextField
import com.example.dosediary.ui.theme.Primary

@Composable
fun Profile(navController: NavController) {
    Scaffold(
        topBar = {
            CustomTopAppBar(
                header = "Profile",
                showNavigationIcon = true,
                navController = navController,
                imageResId = R.drawable.icon,  // Customizable icon
                imageDescription = "App Icon"
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                item { UserDetail() }
                item { MedicationHistory(navController) }
                item { MedicationDetail(navController)}
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetail(isEdit: Boolean = true) {

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

            if (isEdit) {
                OutlinedTextField(
                    value = "John",
                    onValueChange = { },
                    label = { Text("First Name") },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        unfocusedTextColor = Color.Black,
                        focusedBorderColor = OutlineTextField, // Custom focused border color
                        unfocusedBorderColor = OutlineTextField // Custom unfocused border color
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = "Doe",
                    onValueChange = { },
                    label = { Text("Last Name") },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        unfocusedTextColor = Color.Black,
                        focusedBorderColor = OutlineTextField, // Custom focused border color
                        unfocusedBorderColor = OutlineTextField // Custom unfocused border color
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = "John.Doe@gmail.com",
                    onValueChange = { },
                    label = { Text("Email") },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        unfocusedTextColor = Color.Black,
                        focusedBorderColor = OutlineTextField, // Custom focused border color
                        unfocusedBorderColor = OutlineTextField // Custom unfocused border color
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = "************",
                    onValueChange = { },
                    label = { Text("Password") },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        unfocusedTextColor = Color.Black,
                        focusedBorderColor = OutlineTextField, // Custom focused border color
                        unfocusedBorderColor = OutlineTextField // Custom unfocused border color
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))

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