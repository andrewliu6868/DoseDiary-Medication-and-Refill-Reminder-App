package com.example.dosediary.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class MedicationHistory (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val timeTaken: String,
    val dateTaken: String,
    val effectiveness: String,
    val ownerId: Int,
    val additionalDetails: String = ""
)