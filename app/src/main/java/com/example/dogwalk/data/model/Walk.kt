package com.example.dogwalk.data.model

data class Walk(
    val id: Long,
    val petName: String,
    val startedAt: Long,
    val durationMinutes: Int,
    val distanceKm: Double,
    val steps: Int
)
