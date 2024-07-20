package com.example.dosediary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import androidx.compose.material3.Surface
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dosediary.state.UserState

import com.example.dosediary.ui.theme.DoseDiaryTheme
import com.example.dosediary.ui.theme.Background
import com.example.dosediary.view.AppEntry
import com.example.dosediary.view.LoginPage
import com.example.dosediary.view.SignUpPage
import com.example.dosediary.viewmodel.LoginViewModel
import com.example.dosediary.viewmodel.MedRefillDetailViewModel
import com.example.dosediary.viewmodel.MedRefillViewModel
import com.example.dosediary.viewmodel.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var userState: UserState

    private val loginViewModel: LoginViewModel by viewModels()
    private val signUpViewModel: SignUpViewModel by viewModels()
    private val medRefillViewModel: MedRefillViewModel by viewModels()
    private val medRefillDetailViewModel: MedRefillDetailViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd")

            val start_calendar = Calendar.getInstance()
            val end_calendar = Calendar.getInstance()
            start_calendar.add(Calendar.DAY_OF_YEAR, 2)
            end_calendar.add(Calendar.DAY_OF_YEAR, 30)

        }

        setContent {
            DoseDiaryTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = Background) {

                    // HomeScreen(_medRefillViewModel, _medRefillDetailViewModel)
                    val navController = rememberNavController()
                    LoginNavigation(navController, loginViewModel, signUpViewModel, medRefillViewModel, medRefillDetailViewModel)

                }
            }
        }


    }
}
@Composable
fun LoginNavigation(
    navController: NavHostController,
    loginViewModel: LoginViewModel,
    signUpViewModel: SignUpViewModel,
    medRefillViewModel: MedRefillViewModel,
    medRefillDetailViewModel: MedRefillDetailViewModel
) {
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginPage(navController, loginViewModel)
        }
        composable("home") {
            AppEntry(medRefillViewModel, medRefillDetailViewModel)
        }
        composable("signup"){
            SignUpPage(navController, viewModel = signUpViewModel)
        }
    }
}


