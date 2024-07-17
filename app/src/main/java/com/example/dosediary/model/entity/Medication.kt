package com.example.dosediary.model.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    foreignKeys = [                         //Mapping "owner" in Medication to "id" in User
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["owner"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Medication(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var medicationName: String = "",
    var startDate: Date = Date(),
    var endDate: Date = Date(),
    var refillDays: Int = 0,
    var dosage: Int = 0,
    var frequency: String = "",
    var owner: Int = 0
)