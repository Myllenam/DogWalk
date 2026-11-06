package com.example.dogwalk.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pets")
data class Pet(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val name: String,

    val breed: String,

    val sex: String,

    val birthDate: String,

    val photoUri: String? = null
)