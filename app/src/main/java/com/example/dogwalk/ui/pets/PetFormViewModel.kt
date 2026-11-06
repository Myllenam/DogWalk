package com.example.dogwalk.ui.pets
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogwalk.data.repository.PetRepository
import com.example.dogwalk.model.Pet
import kotlinx.coroutines.launch

class PetFormViewModel : ViewModel() {


    private val _breeds =
        MutableLiveData<List<String>>()

    val breeds: LiveData<List<String>> =
        _breeds

    private val _loadingBreeds =
        MutableLiveData(false)

    val loadingBreeds: LiveData<Boolean> =
        _loadingBreeds

    fun loadBreeds() {

        viewModelScope.launch {

            try {

                _loadingBreeds.value = true

                val breeds =
                    PetRepository.getBreeds()

                _breeds.value = breeds

            } catch (e: Exception) {

                _breeds.value =
                    emptyList()

            } finally {

                _loadingBreeds.value = false
            }
        }
    }

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