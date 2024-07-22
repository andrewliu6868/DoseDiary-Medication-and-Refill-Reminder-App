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
import com.example.dosediary.viewmodel.LoginViewModel
import com.example.dosediary.viewmodel.UpsertMedicationViewModel
import com.example.dosediary.viewmodel.MedRefillViewModel
import com.example.dosediary.viewmodel.MedicationHistoryViewModel
import com.example.dosediary.viewmodel.MedicationListViewModel
import com.example.dosediary.viewmodel.ProfileViewModel
import com.example.dosediary.viewmodel.SignUpViewModel
import com.example.dosediary.viewmodel.UpsertMedHistoryViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.libraries.places.api.net.PlacesClient

@Composable
fun AppEntry(
    upsertMedHistoryViewModel: UpsertMedHistoryViewModel,
    medicationHistoryViewModel: MedicationHistoryViewModel,
    upsertMedicationViewModel: UpsertMedicationViewModel,
    medicationListViewModel: MedicationListViewModel,
    signUpViewModel: SignUpViewModel,
    loginViewModel: LoginViewModel,
    profileViewModel: ProfileViewModel,
    medRefillViewModel: MedRefillViewModel,
    placesClient: PlacesClient,
    fusedLocationClient: FusedLocationProviderClient
) {
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
            MainAppNavigation(
                upsertMedHistoryViewModel,
                medicationHistoryViewModel,
                upsertMedicationViewModel,
                medicationListViewModel,
                signUpViewModel,
                loginViewModel,
                profileViewModel,
                medRefillViewModel,
                navController,
                placesClient,
                fusedLocationClient
            )
        }
    }
}


@Composable
fun MainAppNavigation (
    upsertMedHistoryViewModel: UpsertMedHistoryViewModel,
    medicationHistoryViewModel: MedicationHistoryViewModel,
    upsertMedicationViewModel: UpsertMedicationViewModel,
    medicationListViewModel: MedicationListViewModel,
    signUpViewModel: SignUpViewModel,
    loginViewModel: LoginViewModel,
    profileViewModel: ProfileViewModel ,
    medRefillViewModel: MedRefillViewModel,
    navController: NavHostController,
    placesClient: PlacesClient,
    fusedLocationClient: FusedLocationProviderClient
){
    //View Model
//    val addMedicationViewModel = hiltViewModel<UpsertMedicationViewModel>()
//    val medicationHistoryViewModel = hiltViewModel<MedicationHistoryViewModel>()
//    val medRefillViewModel = hiltViewModel<MedRefillViewModel>()
//    val profileViewModel = hiltViewModel<ProfileViewModel, >()
//    val medicationListViewModel = hiltViewModel<MedicationListViewModel>()
//    val UpsertMedHistoryViewModel = hiltViewModel<UpsertMedHistoryViewModel>()

    upsertMedicationViewModel.placesClient = placesClient
    upsertMedicationViewModel.fusedLocationClient = fusedLocationClient

    //State
    val addMedicationState = upsertMedicationViewModel.state.collectAsState().value
    val medicationHistoryState = medicationHistoryViewModel.state.collectAsState().value
    val medRefillState = medRefillViewModel.state.collectAsState().value
    val profileState = profileViewModel.state.collectAsState().value
    val medicationListState = medicationListViewModel.state.collectAsState().value
    val upsertMedHistoryState = upsertMedHistoryViewModel.state.collectAsState().value

    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginPage(loginViewModel, navController)}
        composable("signup") { SignUpPage(signUpViewModel, navController)}
        composable("home") { HomePage(navController, medicationListState, medRefillState, medicationListViewModel, medRefillViewModel::onEvent, medicationHistoryViewModel)} //Todo
        composable("refill") { MedicationRefillPage(navController, medRefillState, medRefillViewModel::onEvent) }
        composable("history") { MedicationHistoryPage(navController, medicationHistoryState, medicationHistoryViewModel::onEvent)}
        composable("profile") { ProfilePage(navController, profileState, profileViewModel::onEvent) }
        composable("medication") { MedicationListPage(navController, medicationListState, medicationListViewModel::onEvent) }
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

@Composable
fun getCurrentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}