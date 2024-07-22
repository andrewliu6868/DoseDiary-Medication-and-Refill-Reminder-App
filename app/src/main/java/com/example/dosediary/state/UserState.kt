package com.example.dosediary.state

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import com.example.dosediary.model.entity.User
import javax.inject.Singleton

@Singleton
data class UserState (
    val mainUser: User? = User(),
    val currentUser: User? = User(),
    val managedUsers: List<User> = emptyList()
)