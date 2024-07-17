package com.example.dosediary.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface UserRelationshipDao {
    @Upsert
    suspend fun upsertUserRelationship(userRelationship: UserRelationship)

    @Delete
    suspend fun deleteUserRelationship(userRelationship: UserRelationship)

    @Query("SELECT * FROM UserRelationship WHERE mainUserId = :mainUserId")
    fun getUserRelationshipsByMainUserId(mainUserId: Int): Flow<List<UserRelationship>>
}