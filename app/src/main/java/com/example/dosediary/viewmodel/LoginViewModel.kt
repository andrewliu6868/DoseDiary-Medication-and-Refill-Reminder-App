package com.example.dosediary.viewmodel

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.dosediary.model.DoseDiaryDatabase
import com.example.dosediary.model.Medication
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.*

class LoginViewModel(application: Application): ViewModel(){
    private val _userDao = DoseDiaryDatabase.getInstance(application).userDao
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> get() = _loginState

    fun login(email : String, password : String){
        viewModelScope.launch {
            try{
                _loginState.value = LoginState.Loading
                val currUser  = _userDao.validateEmailPassword(email, password).firstOrNull()
                if(currUser != null){
                    _loginState.value= LoginState.Success(currUser)
                } else{
                    _loginState.value = LoginState.Error("User does not exist")
                }
            }catch (e: Exception){
                _loginState.value = LoginState.Error(e.message ?: "Unknown Error" )
            }

        }
    }

    fun resetLoginState() {
        _loginState.value = LoginState.Idle
    }

}
class LoginViewModelFactory(private val application: Application) : ViewModelProvider.Factory{
    override fun <T: ViewModel> create(modelClass: Class<T>): T{
        if(modelClass.isAssignableFrom(LoginViewModel::class.java)){
            return LoginViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}