package com.example.dosediary.event

import com.example.dosediary.model.entity.User

sealed interface LoginEvent {
    data class OnEmailChanged(val email: String) : LoginEvent
    data class OnPasswordChanged(val password: String) : LoginEvent
    object TogglePasswordVisibility : LoginEvent

    object Login : LoginEvent
}