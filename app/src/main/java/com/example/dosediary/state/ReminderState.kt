package com.example.dosediary.state

import com.example.dosediary.model.entity.User

sealed class ReminderState {

    object Idle : ReminderState()
    data class Success(val message: String) : ReminderState()
    data class Error(val message:String) : ReminderState()

    val error: String
        get() = (this as Error).message

}