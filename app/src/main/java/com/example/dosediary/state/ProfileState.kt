package com.example.dosediary.state

import com.example.dosediary.model.entity.User

data class ProfileState (
    val user: User = User(),
    val userProfiles: List<User> = emptyList()
)