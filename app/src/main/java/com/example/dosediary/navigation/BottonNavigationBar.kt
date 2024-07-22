package com.example.dosediary.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MedicalServices
import androidx.compose.material.icons.outlined.Medication
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.dosediary.R
import com.example.dosediary.ui.theme.Background

val items = listOf(
    BottomNavigation(
        titleResId = R.string.home,
        icon = Icons.Outlined.Home
    ),
    BottomNavigation(
        titleResId = R.string.medications,
        icon = Icons.Outlined.Medication
    ),
    BottomNavigation(
        titleResId = R.string.refill,
        icon = Icons.Outlined.MedicalServices
    ),
    BottomNavigation(
        titleResId = R.string.history,
        icon = Icons.Outlined.History
    ),
    BottomNavigation(
        titleResId = R.string.profile,
        icon = Icons.Outlined.AccountCircle
    )
)

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    var selectedItem by remember { mutableIntStateOf(0) }
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    // Update selectedItem based on currentRoute
    selectedItem = when (currentRoute) {
        "home" -> 0
        "medication", "add_medication" -> 1
        "refill" -> 2
        "history", "editMedication" -> 3
        "profile" -> 4
        else -> 0
    }

    NavigationBar {
        Row(
            modifier = Modifier.background(color = Background)
        ) {
            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    selected = selectedItem == index,
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = stringResource(id = item.titleResId),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(id = item.titleResId),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    },
                    onClick = {
                        selectedItem = index
                        when (item.titleResId) {
                            R.string.home -> navController.navigate("home")
                            R.string.medications -> navController.navigate("medication")
                            R.string.refill -> navController.navigate("refill")
                            R.string.history -> navController.navigate("history")
                            R.string.profile -> navController.navigate("profile")
                        }
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Bottom Navigation Preview")
@Composable
fun BottomNavigationBarPreview() {
    val navController = rememberNavController()
    BottomNavigationBar(navController)
}
