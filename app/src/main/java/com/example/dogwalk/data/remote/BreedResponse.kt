package com.example.dogwalk.data.remote

data class BreedResponse(
    val message: Map<String, List<String>>,
    val status: String
)