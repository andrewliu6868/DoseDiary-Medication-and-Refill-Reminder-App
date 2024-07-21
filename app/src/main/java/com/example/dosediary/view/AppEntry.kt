package com.example.dosediary.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dosediary.navigation.BottomNavigationBar
import com.example.dosediary.viewmodel.AddMedicationViewModel
import com.example.dosediary.viewmodel.MedRefillViewModel
import com.example.dosediary.viewmodel.MedicationHistoryViewModel
import com.example.dosediary.viewmodel.MedicationListViewModel
import com.example.dosediary.viewmodel.ProfileViewModel

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
    //View Model
    val addMedicationViewModel = hiltViewModel<AddMedicationViewModel>()
    val medicationHistoryViewModel = hiltViewModel<MedicationHistoryViewModel>()
    val medRefillViewModel = hiltViewModel<MedRefillViewModel>()
    val profileViewModel = hiltViewModel<ProfileViewModel>()
    val medicationListViewModel = hiltViewModel<MedicationListViewModel>()

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
        composable("profile") { ProfilePage(navController, profileState, profileViewModel::onEvent) }
        composable("medication") { MedicationListPage(navController, medicationListState, medicationListViewModel::onEvent) }
        composable("Add Medication") { AddMedicationPage(navController, addMedicationState, addMedicationViewModel::onEvent) }
        composable("Edit Medication") {
            val selectedMedication = medicationListState.selectedMedicationDetail
            LaunchedEffect(selectedMedication) {
                addMedicationViewModel.initialize(selectedMedication)
            }
            AddMedicationPage(navController, addMedicationState, addMedicationViewModel::onEvent)
        }
        //RefillDetails: State is the Med Refill State, No Event because this page is just displaying, no input from user.
        composable("refillDetails") { MedicationRefillDetailPage(navController, medRefillState) }
        composable("editMedication") { UpsertMedicationPage(navController) }  //Todo
    }
}