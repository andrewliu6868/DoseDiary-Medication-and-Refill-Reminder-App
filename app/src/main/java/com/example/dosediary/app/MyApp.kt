package com.example.dosediary.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp : Application() {

    override fun onCreate(){
        super.onCreate()
        createNotificationChannel()
    }

    // declare in application class because we only want one instance
    private fun createNotificationChannel(){
        val name = "Medication Reminder"
        val descriptionText = "Channel for medication reminders"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel("REMINDER_CHANNEL", name, importance).apply {
            description=descriptionText
        }
        val notificationManager: NotificationManager = getSystemService(NotificationManager::class.java) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}