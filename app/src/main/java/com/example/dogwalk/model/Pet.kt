package com.example.dogwalk.model

data class Pet(
    val id: Long = 0,
    val name: String,
    val breed: String,
    val sex: String,
    val birthDate: String,
    val photoUri: String? = null
)