package com.example.dosediary.event

import com.example.dosediary.model.entity.User

sealed interface UserEvent {
    data class SetMainUser(val user: User) : UserEvent
    data class SetCurrentUser(val user: User) : UserEvent
    data class SetManagedUsers(val users: List<User>) : UserEvent
}