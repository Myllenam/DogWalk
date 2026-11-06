package com.example.dogwalk.ui.pets

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.dogwalk.data.local.database.DogWalkDatabase
import com.example.dogwalk.data.repository.PetRepository
import com.example.dogwalk.model.Pet
import kotlinx.coroutines.launch

class PetFormViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository: PetRepository

    private val _pet =
        MutableLiveData<Pet?>()

    val pet: LiveData<Pet?> =
        _pet

    private val _breeds =
        MutableLiveData<List<String>>()

    val breeds: LiveData<List<String>> =
        _breeds

    private val _loadingBreeds =
        MutableLiveData(false)

    val loadingBreeds: LiveData<Boolean> =
        _loadingBreeds

    init {

        val petDao =
            DogWalkDatabase
                .getDatabase(application)
                .petDao()

        repository =
            PetRepository(petDao)
    }


    // -------------------
    // ROOM
    // -------------------

    fun loadPet(id: Long) {

        viewModelScope.launch {

            _pet.value =
                repository.getPetById(id)
        }
    }

    fun savePet(
        id: Long,
        name: String,
        breed: String,
        sex: String,
        birthDate: String,
        photoUri: String?
    ) {

        viewModelScope.launch {

            val pet = Pet(
                id = id,
                name = name,
                breed = breed,
                sex = sex,
                birthDate = birthDate,
                photoUri = photoUri
            )

            repository.savePet(pet)
        }
    }


    // -------------------
    // RETROFIT
    // -------------------

    fun loadBreeds() {

        viewModelScope.launch {

            try {

                _loadingBreeds.value = true

                _breeds.value =
                    repository.getBreeds()

            } catch (e: Exception) {

                _breeds.value =
                    emptyList()

            } finally {

                _loadingBreeds.value = false
            }
        }
    }
}