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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.dosediary.utils.DoseDiaryDatabase
import com.example.dosediary.model.entity.Medication
import com.example.dosediary.model.entity.User
import com.example.dosediary.model.UserState

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
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        lifecycleScope.launch {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd")

            val start_calendar = Calendar.getInstance()
            val end_calendar = Calendar.getInstance()
            start_calendar.add(Calendar.DAY_OF_YEAR, 2)
            end_calendar.add(Calendar.DAY_OF_YEAR, 30)


//            val sampleMedications = listOf(
//                Medication(
//                    medicationName = "Medication 1",
//                    startDate = dateFormat.parse(dateFormat.format(start_calendar.time)) ?: Date(),
//                    endDate = dateFormat.parse(dateFormat.format(end_calendar.time)) ?: Date(),
//                    refillDays = 10,
//                    dosage = 1,
//                    frequency = "Daily",
//                    owner = 1
//                ),
//                Medication(
//                    medicationName = "Medication 2",
//                    startDate = dateFormat.parse(dateFormat.format(start_calendar.time)) ?: Date(),
//                    endDate = dateFormat.parse(dateFormat.format(start_calendar.time)) ?: Date(),
//                    refillDays = 10,
//                    dosage = 1,
//                    frequency = "Daily",
//                    owner = 1
//                ),
//                Medication(
//                    medicationName = "Medication 3",
//                    startDate = dateFormat.parse(dateFormat.format(start_calendar.time)) ?: Date(),
//                    endDate = dateFormat.parse(dateFormat.format(start_calendar.time)) ?: Date(),
//                    refillDays = 10,
//                    dosage = 1,
//                    frequency = "Daily",
//                    owner = 1
//                ),
//            )

            val user = User(
                firstName = "daniel",
                lastname = "an",
                email = "daniel@gmail.com",
                password = "1234"
            )

            // create sub users for user daniel
            val subUserList = listOf(
                User(
                    firstName = "sub1",
                    lastname = "an",
                    email = "sub1@gmail.com",
                    password = "password"
                ),
                User(
                    firstName = "sub1",
                    lastname = "an",
                    email = "sub1@gmail.com",
                    password = "password"
                )
            )

            // insert user daniel into the database
            val userDao = DoseDiaryDatabase.getInstance(application).userDao
//            userDao.upsertUser(user)

            // set user daniel as the current user
            val user1 = userDao.getUserById(1).firstOrNull() ?: User()
            userState.setMainUser(user1)
            userState.setcurrentUser(user1)

            val userRelationshipDao = DoseDiaryDatabase.getInstance(application).userRelationshipDao
            val user1Relationships = userRelationshipDao.getUserRelationshipsByMainUserId(1).firstOrNull() ?: emptyList()
            userState.setMangedUsers(listOf(user1) + user1Relationships.map { userRelationship ->
                userDao.getUserById(userRelationship.subUserId).firstOrNull() ?: User()
            })

            //insert sub users into the database
//            subUserList.forEach() {
//                userDao.upsertUser(it)
//            }
//            // establish relationship between user daniel and sub users
//            val userRelationshipDao = DoseDiaryDatabase.getInstance(application).userRelationshipDao
//            userRelationshipDao.upsertUserRelationship(
//                UserRelationship(
//                    mainUserId = 1,
//                    subUserId = 8
//                )
//            )
//            userRelationshipDao.upsertUserRelationship(
//                UserRelationship(
//                    mainUserId = 1,
//                    subUserId = 9
//                )
//            )


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
                    HomeScreen()
                }
            }
        }


    }
}

@OptIn(ExperimentalMaterial3Api::class)
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
fun Navigation (navController: NavHostController){

    NavHost(navController = navController, startDestination = "home") {
        composable("home") { MedicationListScreen(navController)}
        composable("refill") { MedicationRefillScreen(navController) }
        composable("history") { MedicationHistory(navController) }
        composable("profile") { Profile(navController) }
        composable("Add Medication") { AddMedicationPage(navController) }
        composable(
            "refillDetails/{medicationId}",
            arguments = listOf(navArgument("medicationId") { type = NavType.IntType })
        ) { backStackEntry ->
            val medicationId = backStackEntry.arguments?.getInt("medicationId")?:0
            MedicationRefillDetailScreen(navController, medicationId)
        }
        composable("editMedication") { EditMedication(navController) }
    }
}

