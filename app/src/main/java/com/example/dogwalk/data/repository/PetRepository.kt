package com.example.dogwalk.data.repository

import com.example.dogwalk.model.Pet

object PetRepository {

    private val pets = mutableListOf(
        Pet(
            id = 1,
            name = "Toby",
            breed = "Golden Retriever",
            sex = "Macho",
            birthDate = "14/03/2022"
        ),
        Pet(
            id = 2,
            name = "Mel",
            breed = "Vira-lata",
            sex = "Fêmea",
            birthDate = "10/05/2024"
        ),
        Pet(
            id = 3,
            name = "Luna",
            breed = "Border Collie",
            sex = "Fêmea",
            birthDate = "20/08/2023"
        )
    )

    fun getPets(): List<Pet> {
        return pets.toList()
    }

    fun getPetById(id: Long): Pet? {
        return pets.find { it.id == id }
    }

    fun savePet(pet: Pet) {

        if (pet.id == 0L) {

            val nextId =
                (pets.maxOfOrNull { it.id } ?: 0) + 1

            pets.add(
                pet.copy(id = nextId)
            )

        } else {

            val index =
                pets.indexOfFirst { it.id == pet.id }

            if (index != -1) {
                pets[index] = pet
            }
        }
    }
}