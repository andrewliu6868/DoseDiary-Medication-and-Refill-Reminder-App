package com.example.dosediary.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dosediary.model.DoseDiaryDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.*

class SignUpViewModel(application: Application): ViewModel(){
    private val _userDao = DoseDiaryDatabase.getInstance(application).userDao
    private val _signUpState = MutableStateFlow<SignUpState>(SignUpState.Idle)
    val signUpState: StateFlow<SignUpState> get() = _signUpState
    // check if email already exists


}
class SignUpViewModelFactory(private val application: Application) : ViewModelProvider.Factory{
    override fun <T: ViewModel> create(modelClass: Class<T>): T{
        if(modelClass.isAssignableFrom(SignUpViewModel::class.java)){
            return SignUpViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}