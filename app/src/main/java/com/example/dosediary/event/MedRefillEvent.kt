package com.example.dosediary.event

sealed interface MedRefillEvent {
    object SetRefillCompleted : MedRefillEvent
}