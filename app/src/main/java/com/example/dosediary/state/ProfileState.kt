package com.example.dosediary.state

import com.example.dosediary.model.entity.User

data class ProfileState (
    val mainUser: User? = null,
    val currentUser: User? = null,
    val managedUsers: List<User> = emptyList(),
    val addUserFirstName: String = "",
    val addUserLastName: String = "",
    val isAddingUser: Boolean = false,
    val editCurrentUserFirstName: String = "",
    val editCurrentUserLastName: String = "",
    val editCurrentUserEmail: String = "",
    val editCurrentUserPassword: String = "",
    val showDeleteConfirmationDialog: Boolean = false,
)