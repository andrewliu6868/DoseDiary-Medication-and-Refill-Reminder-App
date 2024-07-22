package com.example.dosediary.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.dosediary.R
import com.example.dosediary.ui.theme.Background

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(
    header: String,
    showNavigationIcon: Boolean,
    navController: NavController,
    @DrawableRes imageResId: Int? = null,
    imageDescription: String = "App Logo",
    onPdfButtonClick: (() -> Unit)? = null,
    onLanguageButtonClick: (() -> Unit)? = null
) {
    CenterAlignedTopAppBar(
        modifier = Modifier
            .padding(vertical = 16.dp)
            .background(color = Background),
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Background
        ),
        title = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (imageResId != null) {
                        Image(
                            painter = painterResource(id = imageResId),
                            contentDescription = imageDescription,
                            modifier = Modifier.width(25.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = header,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                    )
                }
            }
        },
        navigationIcon = {
            if (showNavigationIcon) {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Navigate Up"
                    )
                }
            }
        },
        actions = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (onPdfButtonClick != null) {
                    IconButton(onClick = onPdfButtonClick) {
                        Icon(
                            imageVector = Icons.Filled.BarChart,
                            contentDescription = "Generate PDF"
                        )
                    }
                }
                if (onLanguageButtonClick != null) {
                    IconButton(onClick = onLanguageButtonClick) {
                        Icon(
                            imageVector = Icons.Filled.Language,
                            contentDescription = "Select Language"
                        )
                    }
                }
                if (showNavigationIcon && onPdfButtonClick == null && onLanguageButtonClick == null) {
                    Spacer(modifier = Modifier.width(50.dp)) // Adjust width to balance space
                }
            }
        }
    )
}

@Preview(showBackground = true, name = "TopAppBar Preview")
@Composable
fun CustomTopAppBarPreview() {
    val navController = rememberNavController()

    CustomTopAppBar(
        header = "My App",
        showNavigationIcon = true,
        navController = navController,
        imageResId = R.drawable.icon,
        imageDescription = "Image",
        onPdfButtonClick = { },
        onLanguageButtonClick = { }
    )
}
