package com.example.dosediary.model

import kotlinx.serialization.Serializable

@Serializable
data class Medicine (
    val name: String,
    val timeTaken: String,
    val dateTaken: String,
    val effectiveness: String
)