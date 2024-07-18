package com.example.dosediary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocal
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.dosediary.model.DoseDiaryDatabase
import com.example.dosediary.model.Medication
import com.example.dosediary.model.User
import com.example.dosediary.model.UserRepository

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
import com.example.dosediary.view.SignUpScreen
import com.example.dosediary.viewmodel.LoginState
import com.example.dosediary.viewmodel.LoginViewModel
import com.example.dosediary.viewmodel.LoginViewModelFactory
import com.example.dosediary.viewmodel.MedRefillDetailViewModel
import com.example.dosediary.viewmodel.MedRefillDetailViewModelFactory
import com.example.dosediary.viewmodel.MedRefillViewModel
import com.example.dosediary.viewmodel.MedRefillViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var userState: UserState

    private val  medRefillDetailViewModel by viewModels<MedRefillDetailViewModel>{
        MedRefillDetailViewModelFactory(application)
    }

    private val medRefillViewModel by viewModels<MedRefillViewModel> {
        MedRefillViewModelFactory(application, userState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        lifecycleScope.launch {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd")

            val start_calendar = Calendar.getInstance()
            val end_calendar = Calendar.getInstance()
            start_calendar.add(Calendar.DAY_OF_YEAR, 2)
            end_calendar.add(Calendar.DAY_OF_YEAR, 30)


            val sampleMedications = listOf(
                Medication(
                    medicationName = "Medication 1",
                    startDate = dateFormat.parse(dateFormat.format(start_calendar.time)) ?: Date(),
                    endDate = dateFormat.parse(dateFormat.format(end_calendar.time)) ?: Date(),
                    refillDays = 10,
                    dosage = 1,
                    frequency = "Daily",
                    owner = 1
                ),
                Medication(
                    medicationName = "Medication 2",
                    startDate = dateFormat.parse(dateFormat.format(start_calendar.time)) ?: Date(),
                    endDate = dateFormat.parse(dateFormat.format(start_calendar.time)) ?: Date(),
                    refillDays = 10,
                    dosage = 1,
                    frequency = "Daily",
                    owner = 1
                ),
                Medication(
                    medicationName = "Medication 3",
                    startDate = dateFormat.parse(dateFormat.format(start_calendar.time)) ?: Date(),
                    endDate = dateFormat.parse(dateFormat.format(start_calendar.time)) ?: Date(),
                    refillDays = 10,
                    dosage = 1,
                    frequency = "Daily",
                    owner = 1
                ),
            )

            val user = User(
                firstName = "daniel",
                lastname = "an",
                email = "daniel@gmail.com",
                password = "1234"
            )

            val userDao = DoseDiaryDatabase.getInstance(application).userDao
//            userDao.upsertUser(user)
            val user1 = userDao.getUserById(1).firstOrNull() ?: User()
            userState.setUser(user1)
//
//
//            val medicationDao = DoseDiaryDatabase.getInstance(application).medicationDao
//
//            sampleMedications.forEach() {
//                medicationDao.upsertMedication(it)
//            }
        }

        setContent {
            DoseDiaryTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = Background) {
                    // HomeScreen(_medRefillViewModel, _medRefillDetailViewModel)
                    val navController = rememberNavController()
                    AppNavigation(navController, _loginViewModel, _signUpViewModel, _medRefillViewModel, _medRefillDetailViewModel)


                }
            }
        }


    }
}
@Composable
fun AppNavigation(
    navController: NavHostController,
    loginViewModel: LoginViewModel,
    signUpViewModel: SignUpViewModel,
    medRefillViewModel: MedRefillViewModel,
    medRefillDetailViewModel: MedRefillDetailViewModel
) {
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(navController, loginViewModel)
        }
        composable("home") {
            HomeScreen(medRefillViewModel, medRefillDetailViewModel)
        }
        composable("signup"){
            SignUpScreen(navController, viewModel = signUpViewModel)
        }
    }
}




