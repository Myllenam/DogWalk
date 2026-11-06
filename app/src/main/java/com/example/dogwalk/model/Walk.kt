package com.example.dogwalk.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "walks",
    foreignKeys = [
        ForeignKey(
            entity = Pet::class,
            parentColumns = ["id"],
            childColumns = ["petId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["petId"])]
)
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
