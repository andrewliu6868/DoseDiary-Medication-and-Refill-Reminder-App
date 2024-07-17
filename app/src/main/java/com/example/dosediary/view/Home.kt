package com.example.dosediary.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.dosediary.navigation.BottomNavigationBar
import com.example.dosediary.viewmodel.MedRefillDetailViewModel
import com.example.dosediary.viewmodel.MedRefillViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(medRefillViewModel: MedRefillViewModel, medRefillDetailViewModel: MedRefillDetailViewModel) {
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
            HomeNavigation(navController, medRefillViewModel,medRefillDetailViewModel)
        }
    }
}


@Composable
fun HomeNavigation (navController: NavHostController, medRefillViewModel: MedRefillViewModel, medRefillDetailViewModel: MedRefillDetailViewModel){
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { MedicationListScreen(navController, medRefillViewModel)}
        composable("refill") { MedicationRefillScreen(navController, medRefillViewModel) }
        composable("history") { MedicationHistory(navController) }
        composable("profile") { Profile(navController) }
        composable("Add Medication") { AddMedicationPage(navController) }
        composable(
            "refillDetails/{medicationId}",
            arguments = listOf(navArgument("medicationId") { type = NavType.IntType })
        ) { backStackEntry ->
            val medicationId = backStackEntry.arguments?.getInt("medicationId")?:0
            MedicationRefillDetailScreen(navController, medRefillDetailViewModel, medicationId)
        }
        composable("editMedication") { EditMedication(navController) }

    }
}