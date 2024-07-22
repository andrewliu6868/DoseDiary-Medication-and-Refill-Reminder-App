package com.example.dosediary.event

import com.example.dosediary.model.entity.User

sealed interface ProfileEvent {
    object AddUser : ProfileEvent
    data class OnAddUserFirstNameChanged(val firstName: String) : ProfileEvent
    data class OnAddUserLastNameChanged(val lastName: String) : ProfileEvent

    data class OnChangeUser(val user: User) : ProfileEvent

    data class OnCurrentUserFirstNameChanged(val firstName: String) : ProfileEvent

    data class OnCurrentUserLastNameChanged(val lastName: String) : ProfileEvent

    data class OnCurrentUserEmailChanged(val email: String) : ProfileEvent

    data class OnCurrentUserPasswordChanged(val password: String) : ProfileEvent

    object updateCurrentUser : ProfileEvent

    object cancelUpdateCurrentUser : ProfileEvent

    object onDeleteCurrentUser : ProfileEvent

    object confirmDeleteCurrentUser : ProfileEvent

    object cancelDeleteCurrentUser : ProfileEvent

    object onUserLogout: ProfileEvent
}