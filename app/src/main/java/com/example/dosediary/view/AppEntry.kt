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
import com.example.dosediary.viewmodel.LoginViewModel
import com.example.dosediary.viewmodel.UpsertMedicationViewModel
import com.example.dosediary.viewmodel.MedRefillViewModel
import com.example.dosediary.viewmodel.MedicationHistoryViewModel
import com.example.dosediary.viewmodel.MedicationListViewModel
import com.example.dosediary.viewmodel.ProfileViewModel
import com.example.dosediary.viewmodel.SignUpViewModel
import com.example.dosediary.viewmodel.UpsertMedHistoryViewModel
import com.example.dosediary.viewmodel.UserViewModel
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
    val userViewModel = hiltViewModel<UserViewModel>()
    val loginViewModel = hiltViewModel<LoginViewModel>()
    val signUpViewModel = hiltViewModel<SignUpViewModel>()
    val upsertMedicationViewModel = hiltViewModel<UpsertMedicationViewModel>()
    val medicationHistoryViewModel = hiltViewModel<MedicationHistoryViewModel>()
    val medRefillViewModel = hiltViewModel<MedRefillViewModel>()
    val profileViewModel = hiltViewModel<ProfileViewModel, >()
    val medicationListViewModel = hiltViewModel<MedicationListViewModel>()
    val upsertMedHistoryViewModel = hiltViewModel<UpsertMedHistoryViewModel>()

    upsertMedicationViewModel.placesClient = placesClient
    upsertMedicationViewModel.fusedLocationClient = fusedLocationClient

    //State
    val userState = userViewModel.state.collectAsState().value
    val loginState = loginViewModel.state.collectAsState().value
    val signUpState = signUpViewModel.state.collectAsState().value
    val addMedicationState = upsertMedicationViewModel.state.collectAsState().value
    val medicationHistoryState = medicationHistoryViewModel.state.collectAsState().value
    val medRefillState = medRefillViewModel.state.collectAsState().value
//    val profileState = profileViewModel.state.collectAsState().value
    val medicationListState = medicationListViewModel.state.collectAsState().value
    val upsertMedHistoryState = upsertMedHistoryViewModel.state.collectAsState().value

    NavHost(navController = navController, startDestination = "home") {
        composable("login") { LoginPage(navController, loginState, loginViewModel::onEvent, userState, userViewModel::onEvent) }
        composable("signup"){ SignUpPage(navController, signUpState, signUpViewModel::onEvent) }
        composable("home") { HomePage(navController, medicationListState, medRefillState, medicationListViewModel, medRefillViewModel::onEvent, medicationHistoryViewModel)} //Todo
        composable("refill") { MedicationRefillPage(navController, medRefillState, medRefillViewModel::onEvent) }
        composable("history") { MedicationHistoryPage(navController, medicationHistoryState, medicationHistoryViewModel::onEvent)}
//        composable("profile") {
//            LaunchedEffect(Unit) {
//                profileViewModel.initialize(userState)
//            }
//            ProfilePage(navController, profileState, profileViewModel::onEvent)
//        }
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
                    upsertMedicationViewModel.initialize(selectedMedication)
                }
            } else if (mode == "add") {
                LaunchedEffect(Unit) {
                    upsertMedicationViewModel.initialize(null)
                }
            }
            UpsertMedicationPage(navController, addMedicationState, upsertMedicationViewModel::onEvent)
        }
        composable("refillDetails") { MedicationRefillDetailPage(navController, medRefillState) }
        composable("UpsertMedHistoryPage?mode={mode}") { backStackEntry ->
            val mode = backStackEntry.arguments?.getString("mode")
            val selectedMedicationHistory = medicationHistoryState.selectedMedicationHistory

            if (mode == "edit" && selectedMedicationHistory != null) {
                LaunchedEffect(selectedMedicationHistory) {
                    upsertMedHistoryViewModel.initialize(selectedMedicationHistory)
                }
            } else if (mode == "add") {
                LaunchedEffect(Unit) {
                    upsertMedHistoryViewModel.initialize(null)
                }
            }
            UpsertMedicationHistoryPage(navController, upsertMedHistoryState, upsertMedHistoryViewModel::onEvent)
        }
    }
}