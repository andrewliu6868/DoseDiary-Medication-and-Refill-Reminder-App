package com.example.dosediary

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Surface
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dosediary.model.DoseDiaryDatabase

import com.example.dosediary.ui.theme.DoseDiaryTheme
import com.example.dosediary.navigation.BottomNavigationBar
import com.example.dosediary.ui.theme.Background
import com.example.dosediary.view.AddMedicationPage
import com.example.dosediary.view.EditMedication
import com.example.dosediary.view.MedicationHistory
import com.example.dosediary.view.MedicationListScreen
import com.example.dosediary.view.Profile
import com.example.dosediary.view.MedicationRefillScreen
import com.example.dosediary.view.MedicationRefillDetailScreen
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userDao = DoseDiaryDatabase.getInstance(this).userDao
        val medicationDao = DoseDiaryDatabase.getInstance(this).medicationDao
        val medicationHistoryDao = DoseDiaryDatabase.getInstance(this).medicationHistoryDao

        setContent {
            DoseDiaryTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = Background) {
                    HomeScreen()
                }
            }
        }


    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun HomeScreen() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Navigation(navController)
        }
    }
}

@Composable
fun Navigation (navController: NavHostController) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { MedicationListScreen(navController)}
        composable("refill") { MedicationRefillScreen(navController) }
        composable("history") { MedicationHistory(navController) }
        composable("profile") { Profile() }
        composable("Add Medication") { AddMedicationPage(navController) }
        composable("refillDetails") { MedicationRefillDetailScreen(navController) }
        composable("editMedication") { EditMedication(navController) }
    }
}

@Preview(showBackground =true, name = "HomeScreen Preview")
@Composable
fun HomeScreenPreview(){
    HomeScreen();
}

