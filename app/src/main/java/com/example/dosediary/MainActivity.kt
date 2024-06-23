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
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.internal.composableLambda
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.example.dosediary.ui.theme.DoseDiaryTheme
import com.example.dosediary.navigation.BottomNavigationBar
import com.example.dosediary.ui.theme.Background
import com.example.dosediary.view.AddMedicationMain
import com.example.dosediary.view.EditMedication
import com.example.dosediary.view.MedicationHistory
import com.example.dosediary.view.MedicationListScreen
import com.example.dosediary.view.Profile
import com.example.dosediary.view.MedicationRefillScreen
import com.example.dosediary.view.MedicationRefillDetailScreen

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DoseDiaryTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = Background) {
                    HomeScreen()
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
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
            MainContent(navController)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainContent(navController: NavHostController) {

    NavHost(navController = navController, startDestination = "home") {
        composable("home") { MedicationListScreen(navController) } // You need to define this Composable
        composable("refill") { MedicationRefillScreen(navController) } // Define this
        composable("history") { MedicationHistory(navController) } // Define this
        composable("profile") { Profile() } // Define this
        composable("Add Medication") { AddMedicationMain(navController) }
        composable("refillDetails") { MedicationRefillDetailScreen(navController) } // Define this
        composable("editMedication") { EditMedication(navController) }
    }
}

