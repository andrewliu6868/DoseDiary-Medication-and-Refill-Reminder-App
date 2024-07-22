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
import com.example.dosediary.state.UserState
import com.example.dosediary.viewmodel.UpsertMedicationViewModel
import com.example.dosediary.viewmodel.MedRefillViewModel
import com.example.dosediary.viewmodel.MedicationHistoryViewModel
import com.example.dosediary.viewmodel.MedicationListViewModel
import com.example.dosediary.viewmodel.ProfileViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.libraries.places.api.net.PlacesClient

@Composable
fun AppEntry(userState: UserState, placesClient: PlacesClient, fusedLocationClient: FusedLocationProviderClient) {
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
            MainAppNavigation(userState, navController, placesClient, fusedLocationClient)
        }
    }
}


@Composable
fun MainAppNavigation (userState: UserState, navController: NavHostController, placesClient: PlacesClient, fusedLocationClient: FusedLocationProviderClient){
    //View Model
    val addMedicationViewModel = hiltViewModel<UpsertMedicationViewModel>()
    val medicationHistoryViewModel = hiltViewModel<MedicationHistoryViewModel>()
    val medRefillViewModel = hiltViewModel<MedRefillViewModel>()
    val profileViewModel = hiltViewModel<ProfileViewModel>()
    val medicationListViewModel = hiltViewModel<MedicationListViewModel>()

    addMedicationViewModel.placesClient = placesClient
    addMedicationViewModel.fusedLocationClient = fusedLocationClient

    //State
    val addMedicationState = addMedicationViewModel.state.collectAsState().value
    val medicationHistoryState = medicationHistoryViewModel.state.collectAsState().value
    val medRefillState = medRefillViewModel.state.collectAsState().value
    val profileState = profileViewModel.state.collectAsState().value
    val medicationListState = medicationListViewModel.state.collectAsState().value

    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomePage(navController, medRefillState, medRefillViewModel::onEvent)} //Todo
        composable("refill") { MedicationRefillPage(navController, medRefillState, medRefillViewModel::onEvent) }
        composable("history") { MedicationHistoryPage(navController, medicationHistoryState, medicationHistoryViewModel::onEvent, addTestEntries = medicationHistoryViewModel::addTestEntries)}
        composable("profile") {
            LaunchedEffect(Unit) {
                profileViewModel.initialize(userState)
            }
            ProfilePage(navController, profileState, profileViewModel::onEvent)
        }
        composable("medication") {
            LaunchedEffect(Unit) {
                userState.currentUser?.let {
                    medicationListViewModel.initialize(it.id)
                }
            }
            MedicationListPage(navController, medicationListState, medicationListViewModel::onEvent, userState)
        }
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
        composable("refillDetails") { MedicationRefillDetailPage(navController, medRefillState) }
        composable("editMedication") { UpsertMedicationPage(navController) }  //Todo
    }
}

@Composable
fun getCurrentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}