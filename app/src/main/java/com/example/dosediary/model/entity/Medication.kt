package com.example.dosediary.model.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
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
    // From Input
    val medicationName: String,
    val startDate: Date,
    val endDate: Date,
    val frequency: String,
    val times: List<Date>,
    val refillDays: Int,
    val note: String,
    val address: String,
    val addressLatLng: LatLng,
    //
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val lastRefilledDate: Date,
    var owner: Int
)