package com.example.dogwalk.ui.pets
import androidx.lifecycle.ViewModel
import com.example.dogwalk.data.repository.PetRepository
import com.example.dogwalk.model.Pet

class PetFormViewModel : ViewModel() {

    fun getPet(id: Long): Pet? {
        return PetRepository.getPetById(id)
    }

    fun savePet(
        id: Long,
        name: String,
        breed: String,
        sex: String,
        birthDate: String,
        photoUri: String?
    ) {

        val pet = Pet(
            id = id,
            name = name,
            breed = breed,
            sex = sex,
            birthDate = birthDate,
            photoUri = photoUri
        )

        PetRepository.savePet(pet)
    }
}