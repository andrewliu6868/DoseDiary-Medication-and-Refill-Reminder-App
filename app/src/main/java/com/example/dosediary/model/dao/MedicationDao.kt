package com.example.dosediary.model.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.dosediary.model.entity.Medication
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface MedicationDao {
    @Upsert
    suspend fun upsertMedication(medication: Medication)

    @Delete
    suspend fun deleteMedication(medication: Medication)

    @Query("SELECT * FROM medication ORDER BY medicationName ASC")
    fun getMedicationOrderedByFirstName(): Flow<List<Medication>>

    @Query("SELECT * FROM medication WHERE owner = :owner ORDER BY medicationName ASC")
    fun getMedicationsByOwner(owner: Int): Flow<List<Medication>>

//    @Query("SELECT * FROM medication ORDER BY refillDays ASC")
//    fun getMedicationByRefill(): Flow<List<Medication>>
  
    @Query("SELECT * FROM medication WHERE id = :medID ORDER BY medicationName ASC LIMIT 1")
    fun getMedicationByID(medID: Int): Flow<Medication>

    //update lastrefilldate
    @Query("UPDATE medication SET lastRefilledDate = :lastRefilledDate WHERE id = :medID")
    suspend fun updateLastRefillDate(medID: Int, lastRefilledDate: Date)

    @Query("DELETE FROM Medication WHERE id = :medicationId")
    suspend fun deleteMedicationById(medicationId: Int)
}