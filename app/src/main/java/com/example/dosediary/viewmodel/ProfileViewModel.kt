package com.example.dosediary.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dosediary.event.ProfileEvent
import com.example.dosediary.utils.DoseDiaryDatabase
import com.example.dosediary.model.entity.User
import com.example.dosediary.state.UserState
import com.example.dosediary.model.entity.UserRelationship
import com.example.dosediary.state.ProfileState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
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

//    private val _mainUser: MutableStateFlow<User?> = userState.mainUser
//    private val _currentUser: MutableStateFlow<User?> = userState.currentUser
//    private val _managedUsers: MutableStateFlow<List<User>> = userState.managedUsers


    private val _mainUser: MutableStateFlow<User?> = userState.mainUser
    private val _currentUser: MutableStateFlow<User?> = userState.currentUser
    private val _managedUsers: MutableStateFlow<List<User>> = userState.managedUsers



    private val _addUserFirstName: MutableStateFlow<String> = MutableStateFlow("")
    private val _addUserLastName: MutableStateFlow<String> = MutableStateFlow("")
    private val _editCurrentUserFirstName: MutableStateFlow<String> = MutableStateFlow(userState.mainUser.value?.firstName ?: "")
    private val _editCurrentUserLastName: MutableStateFlow<String> = MutableStateFlow(userState.mainUser.value?.lastname ?: "")
    private val _editCurrentUserEmail: MutableStateFlow<String> = MutableStateFlow(userState.mainUser.value?.email ?: "")
    private val _editCurrentUserPassword: MutableStateFlow<String> = MutableStateFlow(userState.mainUser.value?.password ?: "")
    private val _showDeleteConfirmationDialog: MutableStateFlow<Boolean> = MutableStateFlow(false)

    init {
        viewModelScope.launch {

            val user1 = userDao.getUserById(1).firstOrNull() ?: User()
            _mainUser.value = user1
            _currentUser.value = user1

            val newRelationShips = userRelationshipDao.getUserRelationshipsByMainUserId(1).first()
            val managedUsers = (listOf(_mainUser.value) + newRelationShips.map { userRelationship ->
                userDao.getUserById(userRelationship.subUserId).firstOrNull() ?: User()
            }).filterIsInstance<User>()

            _managedUsers.value = managedUsers

            _currentUser.collect { currentUser ->
                _editCurrentUserFirstName.value = currentUser?.firstName ?: ""
                _editCurrentUserLastName.value = currentUser?.lastname ?: ""
                _editCurrentUserEmail.value = currentUser?.email ?: ""
                _editCurrentUserPassword.value = currentUser?.password ?: ""
            }
        }
    }

    val state: StateFlow<ProfileState> = combine(
        listOf(_mainUser,
        _currentUser,
        _managedUsers,
        _addUserFirstName,
        _addUserLastName,
        _editCurrentUserFirstName,
        _editCurrentUserLastName,
        _editCurrentUserEmail,
        _editCurrentUserPassword,
        _showDeleteConfirmationDialog)
    ) { list ->
        ProfileState(
            mainUser = list[0] as User?,
            currentUser = list[1] as User?,
            managedUsers = list[2] as List<User>,
            addUserFirstName = list[3] as String,
            addUserLastName = list[4] as String,
            editCurrentUserFirstName = list[5] as String,
            editCurrentUserLastName = list[6] as String,
            editCurrentUserEmail = list[7] as String,
            editCurrentUserPassword = list[8] as String,
            showDeleteConfirmationDialog = list[9] as Boolean
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
            is ProfileEvent.onCurrentUserFirstNameChanged -> {
                _editCurrentUserFirstName.value = event.firstName
            }
            is ProfileEvent.onCurrentUserLastNameChanged -> {
                _editCurrentUserLastName.value = event.lastName
            }
            is ProfileEvent.onCurrentUserEmailChanged -> {
                _editCurrentUserEmail.value = event.email
            }
            is ProfileEvent.onCurrentUserPasswordChanged -> {
                _editCurrentUserPassword.value = event.password
            }
            ProfileEvent.updateCurrentUser -> {
                val firstName = state.value.editCurrentUserFirstName
                val lastName = state.value.editCurrentUserLastName
                val email = state.value.editCurrentUserEmail
                val password = state.value.editCurrentUserPassword

                viewModelScope.launch {
                    userDao.updateUser(state.value.currentUser!!.id, firstName, lastName, email, password)
                    userState.setcurrentUser(userDao.getUserById(state.value.currentUser!!.id).first())

                    val newRelationShips = userRelationshipDao.getUserRelationshipsByMainUserId(state.value.mainUser!!.id).firstOrNull() ?: emptyList()
                    userState.setMangedUsers(listOf(state.value.mainUser ?: User()) + newRelationShips.map { userRelationship ->
                        userDao.getUserById(userRelationship.subUserId).firstOrNull() ?: User()
                    })

//                    userState.setcurrentUser(userDao.getUserById(state.value.mainUser!!.id).first())
                }
            }
            ProfileEvent.cancelUpdateCurrentUser -> {
                _editCurrentUserFirstName.value = state.value.currentUser?.firstName ?: ""
                _editCurrentUserLastName.value = state.value.currentUser?.lastname ?: ""
                _editCurrentUserEmail.value = state.value.currentUser?.email ?: ""
                _editCurrentUserPassword.value = state.value.currentUser?.password ?: ""
            }
            ProfileEvent.onDeleteCurrentUser -> {
                _showDeleteConfirmationDialog.value = true
            }
            ProfileEvent.confirmDeleteCurrentUser -> {
                viewModelScope.launch {
                    userDao.deleteUserById(state.value.currentUser!!.id)
                    userState.setcurrentUser(userDao.getUserById(state.value.mainUser!!.id).first())

                    val newRelationShips = userRelationshipDao.getUserRelationshipsByMainUserId(state.value.mainUser!!.id).firstOrNull() ?: emptyList()
                    userState.setMangedUsers(listOf(state.value.mainUser ?: User()) + newRelationShips.map { userRelationship ->
                        userDao.getUserById(userRelationship.subUserId).firstOrNull() ?: User()
                    })
                }
                _showDeleteConfirmationDialog.value = false
            }
            ProfileEvent.cancelDeleteCurrentUser -> {
                _showDeleteConfirmationDialog.value = false
            }
        }
    }

}