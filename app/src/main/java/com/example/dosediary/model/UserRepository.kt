package com.example.dosediary.model

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserRepository {
    private val _user = MutableStateFlow<User?>(null)
//    private val _mangedUsers = MutableStateFlow<List<User>>(emptyList())

    val users: StateFlow<User?> get() = _user
//    val managedUsers: StateFlow<List<User>> get() = _mangedUsers

    fun setUsers(user: User) {
        _user.value = user
    }

//    fun setMangedUsers(users: List<User>) {
//        _mangedUsers.value = users
//    }


}