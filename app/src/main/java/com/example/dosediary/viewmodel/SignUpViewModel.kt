package com.example.dosediary.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dosediary.model.DoseDiaryDatabase
import com.example.dosediary.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.*

class SignUpViewModel(application: Application): ViewModel(){
    private val _userDao = DoseDiaryDatabase.getInstance(application).userDao
    private val _signUpState = MutableStateFlow<SignUpState>(SignUpState.Idle)
    val signUpState: StateFlow<SignUpState> get() = _signUpState

    fun addUser(firstName: String, lastName: String, email: String, password:String ){
        viewModelScope.launch {
            try{
                _signUpState.value = SignUpState.Loading
                val tempUser = User(0,email,password,firstName,lastName)
                val newUser = _userDao.verifyUserExist(email).first()
                if(newUser == null){
                    // email doesn't exist yet
                    _userDao.upsertUser(tempUser)
                    _signUpState.value = SignUpState.Success(tempUser)
                }else{
                    _signUpState.value = SignUpState.Error("User already exists")
                }
            }catch(e:Exception){
                _signUpState.value = SignUpState.Error(e.message ?: "Unknown Message")
            }
        }
    }
    fun resetSignUpState() {
        _signUpState.value = SignUpState.Idle
    }

}
class SignUpViewModelFactory(private val application: Application) : ViewModelProvider.Factory{
    override fun <T: ViewModel> create(modelClass: Class<T>): T{
        if(modelClass.isAssignableFrom(SignUpViewModel::class.java)){
            return SignUpViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}