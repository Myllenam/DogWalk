package com.example.dogwalk.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "walks")
data class Walk(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val petId: Long? = null,
    val petName: String,
    val startedAt: Long,
    val durationMinutes: Int,
    val distanceKm: Double,
    val steps: Int,
    val durationSeconds: Long = durationMinutes * 60L
)
