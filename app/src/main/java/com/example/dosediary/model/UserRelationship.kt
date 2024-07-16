package com.example.dosediary.model

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "user_relationship",
    primaryKeys = ["mainUserId", "subUserId"],
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["mainUserId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["subUserId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class UserRelationship(
    val mainUserId: Long,
    val subUserId: Long
)