package com.example.dosediary.model.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.dosediary.model.entity.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Upsert
    suspend fun upsertUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("SELECT * FROM User WHERE id = :id")
    fun getUserById(id: Int): Flow<User>

    @Query("UPDATE User SET firstName = :firstName, lastName = :lastName, email = :email, password = :password WHERE id = :id")
    suspend fun updateUser(id: Int, firstName: String, lastName: String, email: String, password: String)

    @Query("SELECT * FROM User WHERE firstName = :firstName AND lastName = :lastName")
    fun getUserByFullName(firstName: String, lastName: String): Flow<User>

    @Query("SELECT * FROM User WHERE email = :email AND password = :password")
    fun getUserByEmailAndPassword(email: String, password:String): Flow<User?>
    @Query("SELECT * FROM User WHERE email = :tryEmail")
    fun verifyUserExist(tryEmail:String): Flow<User?>
}