package com.example.dosediary.viewmodel

import com.example.dosediary.model.User

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val user: User) : LoginState()
    data class Error(val message:String) : LoginState()

    val error: String
        get() = (this as Error).message
}