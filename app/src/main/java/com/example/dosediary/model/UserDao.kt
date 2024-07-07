package com.example.dosediary.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Upsert

@Dao
interface UserDao {
    @Upsert
    suspend fun upsertUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)
}