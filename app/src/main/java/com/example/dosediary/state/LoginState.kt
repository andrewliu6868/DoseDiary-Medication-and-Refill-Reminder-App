package com.example.dosediary.state

import com.example.dosediary.model.entity.User

data class LoginState(
    val email: String = "",
    val password: String = "",
    val errorMessage: String? = null,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val showPassword: Boolean = false,
    val loginUser: User = User(),
    val managedUser: List<User> = emptyList()
)