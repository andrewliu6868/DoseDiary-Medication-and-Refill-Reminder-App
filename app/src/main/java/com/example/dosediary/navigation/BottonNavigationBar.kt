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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.dosediary.navigation.BottomNavigation
import com.example.dosediary.ui.theme.Background


val items = listOf(
    BottomNavigation(
        title = "Home",
        icon = Icons.Outlined.Home
    ),

    BottomNavigation(
        title = "Medications",
        icon = Icons.Outlined.Medication
    ),

    BottomNavigation(
        title = "Refill",
        icon = Icons.Outlined.MedicalServices
    ),

    BottomNavigation(
        title = "History",
        icon = Icons.Outlined.History
    ),

    BottomNavigation(
        title = "Profile",
        icon = Icons.Outlined.AccountCircle
    )
)

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    var selectedItem by remember { mutableIntStateOf(0) }

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
                            contentDescription = item.title,
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    },
                    label = {
                        Text(
                            text = item.title,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    },
                    onClick = {
                        selectedItem = index
                        when (item.title) {
                            "Home" -> navController.navigate("home")
                            "Medications" -> navController.navigate("medication")
                            "Refill" -> navController.navigate("refill")
                            "History" -> navController.navigate("history")
                            "Profile" -> navController.navigate("profile")
                        }
                    }
                )
            }
        }
    }
}

@Preview(showBackground =true, name = "Bottom Navigation Preview")
@Composable
fun BottomNavigationBarPreview(){
    val navController = rememberNavController()
    BottomNavigationBar(navController)
}