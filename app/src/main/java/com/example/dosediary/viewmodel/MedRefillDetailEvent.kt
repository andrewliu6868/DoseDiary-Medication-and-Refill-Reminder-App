package com.example.dosediary.viewmodel

sealed interface MedRefillDetailEvent {
    object clickedMedication : MedRefillDetailEvent
}