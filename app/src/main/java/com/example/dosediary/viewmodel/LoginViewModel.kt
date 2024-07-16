package com.example.dosediary.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LoginViewModel : ViewModel(){
    private val _login = MutableStateFlow(false)
    val login: StateFlow<Boolean> = _login


    fun login(){
        _login.value = true
    }

    fun logout(){
        _login.value = false
    }
}