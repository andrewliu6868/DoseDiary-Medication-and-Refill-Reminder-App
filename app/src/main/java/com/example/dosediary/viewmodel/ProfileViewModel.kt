package com.example.dosediary.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.dosediary.model.DoseDiaryDatabase
import com.example.dosediary.model.User
import com.example.dosediary.model.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    application: Application
): ViewModel() {

    private val userDao = DoseDiaryDatabase.getInstance(application).userDao
    private val userRelationshipDao = DoseDiaryDatabase.getInstance(application).userRelationshipDao

    private val _currentUser = MutableStateFlow<User?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _relationships = userRelationshipDao.getUserRelationshipsByMainUserId(_currentUser.value?.id ?: 0)
        .flatMapConcat { relationships ->
            combine(relationships.map { relationship ->
                userDao.getUserById(relationship.subUserId)
            }) { users ->
                users.toList()
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _state = MutableStateFlow(ProfileState())

    val state = combine(_state, _currentUser, _relationships) { state, currentUser, relationships ->
        state.copy(
            user = currentUser?: User(),
            userProfiles = relationships
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), MedRefillState())

    fun onEvent(event: ProfileEvent) {
        when(event) {
            is ProfileEvent.clickedAddUser -> {
                viewModelScope.launch {
//                    val medRefills = medicationDao.getMedications()
//                    _state.value = MedRefillState(medRefills)
                }
            }
        }
    }

}

class ProfileViewModelFactory(private val application: Application, private val userRepository: UserRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(userRepository, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}