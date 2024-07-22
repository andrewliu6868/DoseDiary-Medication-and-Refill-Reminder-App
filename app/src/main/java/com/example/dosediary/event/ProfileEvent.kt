package com.example.dosediary.event

import com.example.dosediary.model.entity.User

interface ProfileEvent {
    object addUser : ProfileEvent
    data class onAddUserFirstNameChanged(val firstName: String) : ProfileEvent
    data class onAddUserLastNameChanged(val lastName: String) : ProfileEvent

    data class onChangeUser(val user: User) : ProfileEvent

    data class onCurrentUserFirstNameChanged(val firstName: String) : ProfileEvent

    data class onCurrentUserLastNameChanged(val lastName: String) : ProfileEvent

    data class onCurrentUserEmailChanged(val email: String) : ProfileEvent

    data class onCurrentUserPasswordChanged(val password: String) : ProfileEvent

    object updateCurrentUser : ProfileEvent

    object cancelUpdateCurrentUser : ProfileEvent

    object onDeleteCurrentUser : ProfileEvent

    object confirmDeleteCurrentUser : ProfileEvent

    object cancelDeleteCurrentUser : ProfileEvent
}