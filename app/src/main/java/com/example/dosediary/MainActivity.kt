package com.example.dosediary

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dosediary.state.UserState
import com.example.dosediary.ui.theme.Background
import com.example.dosediary.ui.theme.DoseDiaryTheme
import com.example.dosediary.view.AppEntry
import com.example.dosediary.view.LoginPage
import com.example.dosediary.view.SignUpPage
import com.example.dosediary.viewmodel.LoginViewModel
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DoseDiaryTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = Background) {
                    val navController = rememberNavController()
                    LoginNavigation(navController)
                }
            }
        }
    }
}
@Composable
fun LoginNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginPage(navController) }
        composable("home") { AppEntry() }
        composable("signup"){ SignUpPage(navController) }
    }
}




