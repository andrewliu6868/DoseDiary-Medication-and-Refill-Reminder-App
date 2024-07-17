package com.example.dosediary.model

import androidx.room.Database
import androidx.room.RoomDatabase
import android.content.Context
import androidx.room.Room
import androidx.room.TypeConverters

@Database(
//    entities = [User::class, Medication::class, MedicationHistory::class, UserRelationship::class],
    entities = [User::class, Medication::class, MedicationHistory::class, UserRelationship::class],
    version = 4
)
@TypeConverters(TypeConverter::class)
abstract class DoseDiaryDatabase: RoomDatabase() {
    abstract val userDao: UserDao
    abstract val medicationDao: MedicationDao
    abstract val medicationHistoryDao: MedicationHistoryDao
    abstract val userRelationshipDao: UserRelationshipDao

    companion object {
        @Volatile
        private var INSTANCE: DoseDiaryDatabase? = null

        fun getInstance(context: Context): DoseDiaryDatabase {
            synchronized(this) {
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    DoseDiaryDatabase::class.java,
                    "dosediary_db"
                ).fallbackToDestructiveMigration().build().also {
                    INSTANCE = it
                }
            }
        }
    }

}