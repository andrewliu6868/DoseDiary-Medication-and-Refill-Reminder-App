package com.example.dosediary.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MedicalServices
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.dosediary.model.BottomNavigation
import com.example.dosediary.ui.theme.Background


val items = listOf(
    BottomNavigation(
        title = "Home",
        icon = Icons.Outlined.Home
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

@Preview
@Composable
fun BottomNavigationBar() {
    NavigationBar {
        Row(
            modifier = Modifier.background(color = Background)
        ) {

            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    selected = index == 0,
                    onClick = {},
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
                    }
                )
            }
        }
    }
}