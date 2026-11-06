package com.example.dogwalk.ui.pets

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.dogwalk.data.local.database.DogWalkDatabase
import com.example.dogwalk.data.repository.PetRepository
import com.example.dogwalk.model.Pet

class PetsViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository: PetRepository

    val pets: LiveData<List<Pet>>

    init {

        val petDao =
            DogWalkDatabase
                .getDatabase(application)
                .petDao()

        repository =
            PetRepository(petDao)

        pets =
            repository
                .getPets()
                .asLiveData()
    }
}