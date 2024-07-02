package com.example.dosediary.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.dosediary.R


@Composable
fun OldHeader(header: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.icon),
            contentDescription = "Pill Pics",
            modifier = Modifier.width(25.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = header,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif,
            textAlign = TextAlign.Center
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(
    header: String,
    showNavigationIcon: Boolean,
    navController: NavController,
    @DrawableRes imageResId: Int? = null,
    imageDescription: String = "App Logo"
) {
    CenterAlignedTopAppBar(
        modifier = Modifier.padding(vertical = 16.dp),
        title = {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
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
        },
        navigationIcon = {
            if (showNavigationIcon) {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Navigate Up"
                    )
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
        showNavigationIcon = false,
        navController = navController,
        imageResId = R.drawable.icon,
        imageDescription = "Image"
    )
}