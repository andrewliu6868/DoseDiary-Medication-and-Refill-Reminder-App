package com.example.dosediary.model

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [User::class, Medication::class, MedicationHistory::class],
    version = 1
)
abstract class Database: RoomDatabase() {
    abstract val userDao: UserDao
    abstract val medicationDao: MedicationDao
    abstract val medicationHistoryDao: MedicationHistoryDao
}