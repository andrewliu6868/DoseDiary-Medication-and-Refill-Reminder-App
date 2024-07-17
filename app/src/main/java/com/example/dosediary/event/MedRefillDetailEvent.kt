package com.example.dosediary.event

sealed interface MedRefillDetailEvent {
    object clickedMedication : MedRefillDetailEvent
}