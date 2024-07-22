package com.example.dosediary.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.dosediary.state.UserState
import com.example.dosediary.model.entity.User
import com.example.dosediary.state.LoginState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.dosediary.utils.DoseDiaryDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userState: UserState,
    application: Application
): ViewModel(){
    private val _userDao = DoseDiaryDatabase.getInstance(application).userDao
    private val userRelationshipDao = DoseDiaryDatabase.getInstance(application).userRelationshipDao

    private val _mainUser: MutableStateFlow<User?> = userState.mainUser
    private val _currentUser: MutableStateFlow<User?> = userState.currentUser
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> get() = _loginState

    fun login(email : String, password : String){
        viewModelScope.launch {
            try{
                _loginState.value = LoginState.Loading
                val currUser  = _userDao.validateEmailPassword(email, password).firstOrNull()
                if(currUser != null){
                    userState.setcurrentUser(currUser)
                    userState.setMainUser(currUser)
                    _loginState.value= LoginState.Success(currUser)

                    val newRelationShips = userRelationshipDao.getUserRelationshipsByMainUserId(currUser.id).firstOrNull() ?: emptyList()
                    userState.setMangedUsers(listOf(currUser) + newRelationShips.map { userRelationship ->
                        _userDao.getUserById(userRelationship.subUserId).firstOrNull() ?: User()
                    })

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
class LoginViewModelFactory(private val application: Application,private val userState: UserState) : ViewModelProvider.Factory{
    override fun <T: ViewModel> create(modelClass: Class<T>): T{
        if(modelClass.isAssignableFrom(LoginViewModel::class.java)){
            return LoginViewModel(userState, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}