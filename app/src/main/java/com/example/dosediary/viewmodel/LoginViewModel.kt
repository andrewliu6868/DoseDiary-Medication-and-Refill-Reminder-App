package com.example.dosediary.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.dosediary.event.LoginEvent
import com.example.dosediary.event.UserEvent
import com.example.dosediary.model.entity.User
import com.example.dosediary.state.LoginState
import com.example.dosediary.state.UserState
import com.example.dosediary.utils.DoseDiaryDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    application: Application,
    ) : ViewModel() {
    private val userDao = DoseDiaryDatabase.getInstance(application).userDao
    private val userRelationshipDao = DoseDiaryDatabase.getInstance(application).userRelationshipDao
    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.OnEmailChanged -> {
                _state.value = _state.value.copy(email = event.email)
            }
            is LoginEvent.OnPasswordChanged -> {
                _state.value = _state.value.copy(password = event.password)
            }
            is LoginEvent.TogglePasswordVisibility -> {
                _state.value = _state.value.copy(showPassword = !_state.value.showPassword)
            }
            LoginEvent.Login -> {
                login()
            }
        }
    }

    private fun login() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, errorMessage = null)
            try {
                val user = userDao.getUserByEmailAndPassword(_state.value.email, _state.value.password).firstOrNull()
                if (user != null) {
                    _state.value = _state.value.copy(isSuccess = true)
                    _state.value = _state.value.copy(loginUser = user)

                    val newRelationShips = userRelationshipDao.getUserRelationshipsByMainUserId(user.id).firstOrNull() ?: emptyList()
                    _state.value = _state.value.copy(
                        managedUser =  listOf(user) + newRelationShips.map { userRelationship ->
                            userDao.getUserById(userRelationship.subUserId).firstOrNull() ?: User()
                        }
                    )
                } else {
                    _state.value = _state.value.copy(errorMessage = "Invalid email or password")
                }

            } catch (e: Exception) {
                _state.value = _state.value.copy(errorMessage = e.message)
            } finally {
                _state.value = _state.value.copy(isLoading = false)
            }
        }
    }
}

