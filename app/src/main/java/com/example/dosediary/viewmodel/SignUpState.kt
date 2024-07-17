package com.example.dosediary.viewmodel

import com.example.dosediary.model.User


    sealed class SignUpState{
        object Idle : SignUpState()
        object Loading : SignUpState()

        data class Success(val user: User) : SignUpState()

        data class Error(val message: String) : SignUpState()
        val error: String
            get() = (this as Error).message
    }
