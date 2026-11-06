package com.example.dogwalk.data.remote

import retrofit2.http.GET

interface DogApiService {

    @GET("api/breeds/list/all")
    suspend fun getBreeds(): BreedResponse
}