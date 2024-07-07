package com.example.dosediary.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicationDao {
    @Upsert
    suspend fun upsertMedication(medication: Medication)

    @Delete
    suspend fun deleteMedication(medication: Medication)

    @Query("SELECT * FROM medication ORDER BY medicationName ASC")
    fun getContactsOrderedByFirstName(): Flow<List<Medication>>
}