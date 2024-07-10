package com.example.dosediary.viewmodel

sealed interface MedRefillEvent {
    object SetRefillCompleted : MedRefillEvent
}