package com.example.dosediary.viewmodel

import android.app.Application
import androidx.compose.ui.text.style.TextDecoration.Companion.combine
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.dosediary.event.ProfileEvent
import com.example.dosediary.utils.DoseDiaryDatabase
import com.example.dosediary.model.entity.User
import com.example.dosediary.model.UserState
import com.example.dosediary.model.entity.UserRelationship
import com.example.dosediary.state.MedRefillState
import com.example.dosediary.state.ProfileState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userState: UserState,
    application: Application
): ViewModel() {

    private val userDao = DoseDiaryDatabase.getInstance(application).userDao
    private val userRelationshipDao = DoseDiaryDatabase.getInstance(application).userRelationshipDao

    private val _mainUser: MutableStateFlow<User?> = userState.mainUser
    private val _currentUser: MutableStateFlow<User?> = userState.currentUser
    private val _managedUsers: MutableStateFlow<List<User>> = userState.managedUsers
    private val _addUserFirstName: MutableStateFlow<String> = MutableStateFlow("")
    private val _addUserLastName: MutableStateFlow<String> = MutableStateFlow("")
    private val _editMainUserFirstName: MutableStateFlow<String> = MutableStateFlow(userState.mainUser.value?.firstName ?: "")
    private val _editMainUserLastName: MutableStateFlow<String> = MutableStateFlow(userState.mainUser.value?.lastname ?: "")
    private val _editMainUserEmail: MutableStateFlow<String> = MutableStateFlow(userState.mainUser.value?.email ?: "")
    private val _editMainUserPassword: MutableStateFlow<String> = MutableStateFlow(userState.mainUser.value?.password ?: "")

    init {
        viewModelScope.launch {
            _mainUser.collect { mainUser ->
                _editMainUserFirstName.value = mainUser?.firstName ?: ""
                _editMainUserLastName.value = mainUser?.lastname ?: ""
                _editMainUserEmail.value = mainUser?.email ?: ""
                _editMainUserPassword.value = mainUser?.password ?: ""
            }
        }
    }

    val state: StateFlow<ProfileState> = combine(
        listOf(_mainUser,
        _currentUser,
        _managedUsers,
        _addUserFirstName,
        _addUserLastName,
        _editMainUserFirstName,
        _editMainUserLastName,
        _editMainUserEmail,
        _editMainUserPassword)
    ) { list ->
        ProfileState(
            mainUser = list[0] as User?,
            currentUser = list[1] as User?,
            managedUsers = list[2] as List<User>,
            addUserFirstName = list[3] as String,
            addUserLastName = list[4] as String,
            editMainUserFirstName = list[5] as String,
            editMainUserLastName = list[6] as String,
            editMainUserEmail = list[7] as String,
            editMainUserPassword = list[8] as String
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        ProfileState(currentUser = null, managedUsers = emptyList(), mainUser = null, addUserFirstName = "", addUserLastName = "", isAddingUser = false)
    )

    fun onEvent(event: ProfileEvent) {
        when(event) {
            ProfileEvent.addUser -> {
                val firstName = state.value.addUserFirstName
                val lastName = state.value.addUserLastName
                val newUser = User(firstName = firstName, lastname = lastName)

                viewModelScope.launch {
                    userDao.upsertUser(newUser)

                    val insertedNewUser = userDao.getUserByFullName(firstName, lastName).first()
                    val newRelationship = UserRelationship(mainUserId = state.value.mainUser!!.id, subUserId = insertedNewUser.id)
                    userRelationshipDao.upsertUserRelationship(newRelationship)

                    val newRelationShips = userRelationshipDao.getUserRelationshipsByMainUserId(state.value.mainUser!!.id).firstOrNull() ?: emptyList()
                    userState.setMangedUsers(listOf(state.value.mainUser ?: User()) + newRelationShips.map { userRelationship ->
                        userDao.getUserById(userRelationship.subUserId).firstOrNull() ?: User()
                    })
                }

                _addUserFirstName.value = ""
                _addUserLastName.value = ""

            }
            is ProfileEvent.onAddUserFirstNameChanged -> {
                _addUserFirstName.value = event.firstName
            }
            is ProfileEvent.onAddUserLastNameChanged -> {
                _addUserLastName.value = event.lastName
            }
            is ProfileEvent.onChangeUser -> {
                userState.setcurrentUser(event.user)
            }
            is ProfileEvent.onMainUserFirstNameChanged -> {
                _editMainUserFirstName.value = event.firstName
            }
            is ProfileEvent.onMainUserLastNameChanged -> {
                _editMainUserLastName.value = event.lastName
            }
            is ProfileEvent.onMainUserEmailChanged -> {
                _editMainUserEmail.value = event.email
            }
            is ProfileEvent.onMainUserPasswordChanged -> {
                _editMainUserPassword.value = event.password
            }
            ProfileEvent.updateMainUser -> {
                val firstName = state.value.editMainUserFirstName
                val lastName = state.value.editMainUserLastName
                val email = state.value.editMainUserEmail
                val password = state.value.editMainUserPassword

                viewModelScope.launch {
                    userDao.updateUser(state.value.mainUser!!.id, firstName, lastName, email, password)
                    userState.setMainUser(userDao.getUserById(state.value.mainUser!!.id).first())

                    val newRelationShips = userRelationshipDao.getUserRelationshipsByMainUserId(state.value.mainUser!!.id).firstOrNull() ?: emptyList()

                    userState.setMangedUsers(listOf(state.value.mainUser ?: User()) + newRelationShips.map { userRelationship ->
                        userDao.getUserById(userRelationship.subUserId).firstOrNull() ?: User()
                    })

                    userState.setcurrentUser(userDao.getUserById(state.value.mainUser!!.id).first())
                }
            }
            ProfileEvent.cancelUpdateMainUser -> {
                _editMainUserFirstName.value = state.value.mainUser?.firstName ?: ""
                _editMainUserLastName.value = state.value.mainUser?.lastname ?: ""
                _editMainUserEmail.value = state.value.mainUser?.email ?: ""
                _editMainUserPassword.value = state.value.mainUser?.password ?: ""
            }
        }
    }

}