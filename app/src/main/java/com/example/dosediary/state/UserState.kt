package com.example.dosediary.state

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import com.example.dosediary.model.entity.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class UserState {

    private val _mainUser = MutableStateFlow<User?>(null)
    private val _currentUser = MutableStateFlow<User?>(null)
    private val _managedUsers = MutableStateFlow<List<User>>(emptyList())

    val mainUser: MutableStateFlow<User?> get() = _mainUser
    val currentUser: MutableStateFlow<User?> get() = _currentUser
    val managedUsers: MutableStateFlow<List<User>> get() = _managedUsers


    fun setMainUser(user: User) {
        _mainUser.value = user
    }
    fun setcurrentUser(user: User) {
        _currentUser.value = user
    }

    fun setMangedUsers(users: List<User>) {
        _managedUsers.value = users
//        viewModelScope.launch {
//            _managedUsers.flatMapLatest { users ->
//                combine(users.map { user ->
//                    Flow(user)
//                }) { updatedUsers ->
//                    updatedUsers.toList()
//                }
//            }.collect { updatedUsersList ->
//                _managedUsers.value = updatedUsersList
//            }
//        }
    }


}