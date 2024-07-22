package com.example.dosediary.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.dosediary.navigation.BottomNavigationBar
import com.example.dosediary.viewmodel.UpsertMedicationViewModel
import com.example.dosediary.viewmodel.MedRefillViewModel
import com.example.dosediary.viewmodel.MedicationHistoryViewModel
import com.example.dosediary.viewmodel.MedicationListViewModel
import com.example.dosediary.viewmodel.ProfileViewModel
import com.example.dosediary.viewmodel.UpsertMedHistoryViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.libraries.places.api.net.PlacesClient

@Composable
fun AppEntry(placesClient: PlacesClient, fusedLocationClient: FusedLocationProviderClient) {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute in listOf("home", "refill", "history", "medication", "profile")) {
                BottomNavigationBar(navController)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            MainAppNavigation(navController, placesClient, fusedLocationClient)
        }
    }
}


@Composable
fun MainAppNavigation (navController: NavHostController, placesClient: PlacesClient, fusedLocationClient: FusedLocationProviderClient){
    //View Model
    val addMedicationViewModel = hiltViewModel<UpsertMedicationViewModel>()
    val medicationHistoryViewModel = hiltViewModel<MedicationHistoryViewModel>()
    val medRefillViewModel = hiltViewModel<MedRefillViewModel>()
    val profileViewModel = hiltViewModel<ProfileViewModel>()
    val medicationListViewModel = hiltViewModel<MedicationListViewModel>()
    val addMedHistoryViewModel = hiltViewModel<UpsertMedHistoryViewModel>()

    addMedicationViewModel.placesClient = placesClient
    addMedicationViewModel.fusedLocationClient = fusedLocationClient

    //State
    val addMedicationState = addMedicationViewModel.state.collectAsState().value
    val medicationHistoryState = medicationHistoryViewModel.state.collectAsState().value
    val medRefillState = medRefillViewModel.state.collectAsState().value
    val profileState = profileViewModel.state.collectAsState().value
    val medicationListState = medicationListViewModel.state.collectAsState().value
    val addMedHistoryState = addMedHistoryViewModel.state.collectAsState().value

    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginPage(navController)}
        composable("signup") { SignUpPage(navController)}
        composable("home") { HomePage(navController, medRefillState, medRefillViewModel::onEvent)} //Todo
        composable("refill") { MedicationRefillPage(navController, medRefillState, medRefillViewModel::onEvent) }
        composable("history") { MedicationHistoryPage(navController, medicationHistoryState, medicationHistoryViewModel::onEvent, addTestEntries = medicationHistoryViewModel::addTestEntries)}
        composable("profile") { ProfilePage(navController, profileState, profileViewModel::onEvent) }
        composable("medication") { MedicationListPage(navController, medicationListState, medicationListViewModel::onEvent) }
        composable("UpsertMedicationPage?mode={mode}") { backStackEntry ->
            val mode = backStackEntry.arguments?.getString("mode")
            val selectedMedication = medicationListState.selectedMedicationDetail

            if (mode == "edit" && selectedMedication != null) {
                LaunchedEffect(selectedMedication) {
                    addMedicationViewModel.initialize(selectedMedication)
                }
            } else if (mode == "add") {
                LaunchedEffect(Unit) {
                    addMedicationViewModel.initialize(null)
                }
            }

            UpsertMedicationPage(navController, addMedicationState, addMedicationViewModel::onEvent)
        }
        //RefillDetails: State is the Med Refill State, No Event because this page is just displaying, no input from user.
//        composable(
//            "refillDetails/{medicationId}",
//            arguments = listOf(navArgument("medicationId") { type = NavType.IntType })
//        ) { backStackEntry ->
//            val medicationId = backStackEntry.arguments?.getInt("medicationId")?:0
//            MedicationRefillDetailPage(navController, medRefillDetailViewModel, medicationId)
//        }
        composable("refillDetails") { MedicationRefillDetailPage(navController, medRefillState) }
        composable("UpsertMedHistoryPage?mode={mode}") { backStackEntry ->
            val mode = backStackEntry.arguments?.getString("mode")
            val selectedMedicationHistory = medicationHistoryState.selectedMedicationHistory

            if (mode == "edit" && selectedMedicationHistory != null) {
                LaunchedEffect(selectedMedicationHistory) {
                    addMedHistoryViewModel.initialize(selectedMedicationHistory)
                }
            } else if (mode == "add") {
                LaunchedEffect(Unit) {
                    addMedHistoryViewModel.initialize(null)
                }
            }

            UpsertMedicationHistoryPage(navController, addMedHistoryState, addMedHistoryViewModel::onEvent)
        }
    }
}

@Composable
fun getCurrentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}