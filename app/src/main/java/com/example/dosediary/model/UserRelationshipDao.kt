package com.example.dosediary.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Upsert

@Dao
interface UserRelationshipDao {
    @Upsert
    suspend fun upsertUserRelationship(userRelationship: UserRelationship)

    @Delete
    suspend fun deleteUserRelationship(userRelationship: UserRelationship)
}