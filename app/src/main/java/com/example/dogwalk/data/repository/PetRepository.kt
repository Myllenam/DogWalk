package com.example.dogwalk.data.repository

import com.example.dogwalk.data.local.dao.PetDao
import com.example.dogwalk.data.remote.RetrofitClient
import com.example.dogwalk.model.Pet
import kotlinx.coroutines.flow.Flow

// Room
class PetRepository(
    private val petDao: PetDao
) {

    fun getPets(): Flow<List<Pet>> {
        return petDao.getAllPets()
    }

    suspend fun getPetById(id: Long): Pet? {
        return petDao.getPetById(id)
    }

    suspend fun savePet(pet: Pet) {
        petDao.savePet(pet)
    }

    suspend fun deletePet(pet: Pet) {
        petDao.deletePet(pet)
    }

    // RETROFIT

    suspend fun getBreeds(): List<String> {

        val response =
            RetrofitClient.api.getBreeds()

        return response.message
            .flatMap { (breed, subBreeds) ->

                if (subBreeds.isEmpty()) {

                    listOf(
                        breed.replaceFirstChar {
                            it.uppercase()
                        }
                    )

                } else {

                    subBreeds.map { subBreed ->

                        "${
                            subBreed.replaceFirstChar {
                                it.uppercase()
                            }
                        } ${
                            breed.replaceFirstChar {
                                it.uppercase()
                            }
                        }"
                    }
                }
            }
            .sorted()
    }







}