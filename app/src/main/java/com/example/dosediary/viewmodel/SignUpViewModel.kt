package com.example.dosediary.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dosediary.event.SignUpEvent
import com.example.dosediary.state.UserState
import com.example.dosediary.utils.DoseDiaryDatabase
import com.example.dosediary.model.entity.User
import com.example.dosediary.state.SignUpState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    application: Application): ViewModel(){
    private val userDao = DoseDiaryDatabase.getInstance(application).userDao
    private val _state = MutableStateFlow(SignUpState())
    val state: StateFlow<SignUpState> = _state

    fun onEvent(event: SignUpEvent) {
        when (event) {
            is SignUpEvent.OnEmailChanged -> {
                _state.value = _state.value.copy(email = event.email)
            }
            is SignUpEvent.OnPasswordChanged -> {
                _state.value = _state.value.copy(password = event.password)
            }
            is SignUpEvent.OnFirstNameChanged -> {
                _state.value = _state.value.copy(firstName = event.firstName)
            }
            is SignUpEvent.OnLastNameChanged -> {
                _state.value = _state.value.copy(lastName = event.lastName)
            }
            SignUpEvent.SignUp -> {
                signUp()
            }
        }
    }
    private fun signUp() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, errorMessage = null)
            try {
                val newUser = User(
                    email = _state.value.email,
                    password = _state.value.password,
                    firstName = _state.value.firstName,
                    lastName = _state.value.lastName
                )
                userDao.upsertUser(newUser)
                _state.value = _state.value.copy(isSuccess = true)
            } catch (e: Exception) {
                _state.value = _state.value.copy(errorMessage = e.message)
            } finally {
                _state.value = _state.value.copy(isLoading = false)
            }
        }
    }
}