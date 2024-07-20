package com.example.dosediary.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val userState: UserState,
    application: Application): ViewModel(){
    private val _userDao = DoseDiaryDatabase.getInstance(application).userDao
    private val userRelationshipDao = DoseDiaryDatabase.getInstance(application).userRelationshipDao
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
/*class SignUpViewModelFactory(private val application: Application) : ViewModelProvider.Factory{
    override fun <T: ViewModel> create(modelClass: Class<T>): T{
        if(modelClass.isAssignableFrom(SignUpViewModel::class.java)){
            return SignUpViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}*/