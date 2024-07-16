package com.example.dosediary.viewmodel

import com.example.dosediary.model.Medication
import com.example.dosediary.model.User

data class UserState (
    val user: User = User(),
    val userProfiles: List<User> = emptyList()
)