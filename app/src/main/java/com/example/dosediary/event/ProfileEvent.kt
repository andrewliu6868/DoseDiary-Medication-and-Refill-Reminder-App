package com.example.dosediary.event

import com.example.dosediary.model.entity.User

interface ProfileEvent {
    object addUser : ProfileEvent
    data class onAddUserFirstNameChanged(val firstName: String) : ProfileEvent
    data class onAddUserLastNameChanged(val lastName: String) : ProfileEvent

    data class onChangeUser(val user: User) : ProfileEvent

    data class onMainUserFirstNameChanged(val firstName: String) : ProfileEvent

    data class onMainUserLastNameChanged(val lastName: String) : ProfileEvent

    data class onMainUserEmailChanged(val email: String) : ProfileEvent

    data class onMainUserPasswordChanged(val password: String) : ProfileEvent

    object updateMainUser : ProfileEvent

    object cancelUpdateMainUser : ProfileEvent
}