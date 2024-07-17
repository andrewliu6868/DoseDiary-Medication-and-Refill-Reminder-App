package com.example.dosediary.model

import com.example.dosediary.model.entity.User
import kotlinx.coroutines.flow.MutableStateFlow

class UserState {
    private val _user = MutableStateFlow<User?>(null)
//    private val _mangedUsers = MutableStateFlow<List<User>>(emptyList())

    val users: MutableStateFlow<User?> get() = _user
//    val managedUsers: StateFlow<List<User>> get() = _mangedUsers

    fun setUser(user: User) {
        _user.value = user
    }

//    fun setMangedUsers(users: List<User>) {
//        _mangedUsers.value = users
//    }


}