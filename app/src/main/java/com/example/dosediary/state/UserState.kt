package com.example.dosediary.state

import com.example.dosediary.model.entity.User

data class UserState (
    val mainUser: User? = null,
    val currentUser: User? = null,
    val managedUsers: List<User> = emptyList()
)