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

@Composable
fun AppEntry() {
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
            MainAppNavigation(navController)
        }
    }
}


@Composable
fun MainAppNavigation (navController: NavHostController){
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomePage(navController)}
        composable("refill") { MedicationRefillPage() }
        composable("history") { MedicationHistoryPage(navController) }
        composable("profile") { ProfilePage(navController) }
    }
}