package com.example.dosediary.model.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import com.example.dosediary.model.entity.User

@Entity(
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
    val mainUserId: Int,
    val subUserId: Int
)