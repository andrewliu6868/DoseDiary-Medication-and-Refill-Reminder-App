package com.example.dosediary.event

sealed interface SignUpEvent {
    data class OnEmailChanged(val email: String) : SignUpEvent
    data class OnPasswordChanged(val password: String) : SignUpEvent
    data class OnFirstNameChanged(val firstName: String) : SignUpEvent
    data class OnLastNameChanged(val lastName: String) : SignUpEvent
    object SignUp : SignUpEvent
}