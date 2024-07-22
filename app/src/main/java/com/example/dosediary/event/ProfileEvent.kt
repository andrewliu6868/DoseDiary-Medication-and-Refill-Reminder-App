package com.example.dosediary.event

import com.example.dosediary.model.entity.User

sealed interface ProfileEvent {
    object AddUser : ProfileEvent
    data class OnAddUserFirstNameChanged(val firstName: String) : ProfileEvent
    data class OnAddUserLastNameChanged(val lastName: String) : ProfileEvent

    data class OnChangeUser(val user: User) : ProfileEvent

    data class OnMainUserFirstNameChanged(val firstName: String) : ProfileEvent

    data class OnMainUserLastNameChanged(val lastName: String) : ProfileEvent

    data class OnMainUserEmailChanged(val email: String) : ProfileEvent

    data class OnMainUserPasswordChanged(val password: String) : ProfileEvent

    object UpdateMainUser : ProfileEvent

    object CancelUpdateMainUser : ProfileEvent
}