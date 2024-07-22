package com.example.dosediary.components

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.dosediary.R
import com.example.dosediary.ui.theme.Background
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun MedicationReminder(reminder: Triple<String, String, String>, onDismiss: () -> Unit) {
    val (medName, reminderMessage, note) = reminder
    val currTime = LocalDateTime.now()
    val format = DateTimeFormatter.ofPattern("hh:mm a")
    val formatTime = currTime.format(format)

    var expanded by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFF7676)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .clickable { expanded = !expanded },
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    BasicText(
                        text = "$reminderMessage - $formatTime",
                        style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold)
                    )
                    BasicText(text = medName)
                }
                Icon(
                    painter = painterResource(R.drawable.ic_action_name),
                    contentDescription = "dismiss",
                    modifier = Modifier.clickable { onDismiss() }
                )
            }

            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Column(
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text(
                        text = "Notes:",
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = note)
                }
            }
        }
    }
}

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
    // State to control the dialog visibility and message
    val reminders = remember { mutableStateListOf<Triple<String, String, String>>() }

    // BroadcastReceiver to listen for local broadcasts
    val context = LocalContext.current
    val reminderReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val medName = intent?.getStringExtra("Name") ?: "Unknown"
            val message = intent?.getStringExtra("Message") ?: "Time to take your medication!"
            val note = intent?.getStringExtra("Note") ?: "No Notes"
            reminders.add(Triple(medName, message, note))
        }
    }

    // Register the BroadcastReceiver when the Composable is first composed
    DisposableEffect(context) {
        val localBroadcastManager = LocalBroadcastManager.getInstance(context)
        localBroadcastManager.registerReceiver(reminderReceiver, IntentFilter("com.example.dosediary.REMINDER_ALERT"))

        // Cleanup when the Composable leaves the composition
        onDispose {
            localBroadcastManager.unregisterReceiver(reminderReceiver)
        }
    }

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

    // Display the reminders
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp)
    ) {
        reminders.forEach { reminder ->
            MedicationReminder(reminder) {
                reminders.remove(reminder)
            }
        }
    }
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
