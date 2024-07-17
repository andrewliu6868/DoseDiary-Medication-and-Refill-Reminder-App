package com.example.dosediary.model.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.dosediary.model.entity.MedicationHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicationHistoryDao {
    @Upsert
    suspend fun upsertMedicationHistory(medicationHistory: MedicationHistory)

    @Delete
    suspend fun deleteMedicationHistory(medicationHistory: MedicationHistory)

    @Query("SELECT * FROM medicationhistory ORDER BY dateTaken ASC")
    fun getContactsOrderedByFirstName(): Flow<List<MedicationHistory>>
}