package com.example.dosediary.viewmodel

import androidx.lifecycle.ViewModel
import com.example.dosediary.event.UserEvent
import com.example.dosediary.state.UserState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(UserState())
    val state: StateFlow<UserState> = _state

    fun onEvent(event: UserEvent) {
        when (event) {
            is UserEvent.SetMainUser -> {
                _state.value = _state.value.copy(mainUser = event.user)
            }
            is UserEvent.SetCurrentUser -> {
                _state.value = _state.value.copy(currentUser = event.user)
            }
            is UserEvent.SetManagedUsers -> {
                _state.value = _state.value.copy(managedUsers = event.users)
            }
        }
    }
}