package com.example.dosediary.utils

import androidx.room.Database
import androidx.room.RoomDatabase
import android.content.Context
import androidx.room.Room
import androidx.room.TypeConverters
import com.example.dosediary.model.dao.MedicationDao
import com.example.dosediary.model.dao.MedicationHistoryDao
import com.example.dosediary.model.dao.UserDao
import com.example.dosediary.model.dao.UserRelationshipDao
import com.example.dosediary.model.entity.Medication
import com.example.dosediary.model.entity.MedicationHistory
import com.example.dosediary.model.entity.User
import com.example.dosediary.model.entity.UserRelationship

@Database(
//    entities = [User::class, Medication::class, MedicationHistory::class, UserRelationship::class],
    entities = [User::class, Medication::class, MedicationHistory::class, UserRelationship::class],
    version = 11 // Update version if schema changes
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
