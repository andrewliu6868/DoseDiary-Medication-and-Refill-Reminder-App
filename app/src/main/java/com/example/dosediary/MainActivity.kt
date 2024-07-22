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
import androidx.core.content.ContentProviderCompat.requireContext
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
import com.example.dosediary.viewmodel.LoginViewModelFactory
import com.example.dosediary.viewmodel.MedRefillViewModel
import com.example.dosediary.viewmodel.MedRefillViewModelFactory
import com.example.dosediary.viewmodel.ProfileViewModel
import com.example.dosediary.viewmodel.ProfileViewModelFactory
import com.example.dosediary.viewmodel.SignUpViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var userState: UserState

    lateinit var placesClient: PlacesClient
    lateinit var fusedLocationClient: FusedLocationProviderClient

    private val profileViewModel by viewModels<ProfileViewModel> {
        ProfileViewModelFactory(application, userState)
    }

    private val medRefillViewModel by viewModels<MedRefillViewModel> {
        MedRefillViewModelFactory(application, userState)
    }

    private val loginViewModel by viewModels<LoginViewModel> {
        LoginViewModelFactory(application, userState)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(applicationContext)
        Places.initialize(applicationContext, BuildConfig.MAPS_API_KEY)
        placesClient = Places.createClient(applicationContext)

        setContent {
            DoseDiaryTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = Background) {
                    val navController = rememberNavController()
                    LoginNavigation(navController)
                }
            }
        }
    }
    @Composable
    fun LoginNavigation(navController: NavHostController) {
        NavHost(navController = navController, startDestination = "home") {
            composable("login") { LoginPage(loginViewModel, navController) }
            composable("home") { AppEntry(loginViewModel, profileViewModel, medRefillViewModel, placesClient, fusedLocationClient) }
            composable("signup"){ SignUpPage(navController) }
        }
    }
}




