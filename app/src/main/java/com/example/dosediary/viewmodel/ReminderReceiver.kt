package com.example.dosediary.viewmodel

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.dosediary.MainActivity
import com.example.dosediary.R

class ReminderReceiver : BroadcastReceiver() {
    // essentially the call back function
    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, intent:Intent){
        val medName = intent.getStringExtra("Name")
        val message = intent.getStringExtra("Message")

        Log.d("ReminderReceiver", "Received reminder for: $medName")

        val pendingIntent = PendingIntent.getActivity(
            context,0,Intent(context, MainActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, "REMINDER_CHANNEL")
            .setSmallIcon(R.drawable.notification)
            .setContentTitle("Medication Reminder")
            .setContentText("$medName: $message")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(false)
            .build()

        val notificationId = System.currentTimeMillis().toInt()
        Log.d("ReminderReceiver", "Displaying notification with ID: $notificationId")

        with(NotificationManagerCompat.from(context)){
            notify(notificationId,notification)
            Log.d("ReminderReceiver", "Notification displayed for: $medName")
        }
        // in app notification
        val localIntent = Intent("com.example.dosediary.REMINDER_ALERT").apply{
            putExtra("Name", medName)
            putExtra("Message", message)
        }
        LocalBroadcastManager.getInstance(context).sendBroadcast(localIntent)

    }

}