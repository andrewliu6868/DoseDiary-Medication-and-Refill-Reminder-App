package com.example.dosediary.viewmodel

import com.example.dosediary.model.User

data class ProfileState (
    val user: User = User(),
    val userProfiles: List<User> = emptyList()
)