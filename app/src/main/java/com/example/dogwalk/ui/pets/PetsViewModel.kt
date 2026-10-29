package com.example.dogwalk.ui.pets

import androidx.lifecycle.ViewModel
import com.example.dogwalk.data.repository.PetRepository
import com.example.dogwalk.model.Pet

class PetsViewModel : ViewModel() {

    fun getPets(): List<Pet> {
        return PetRepository.getPets()
    }
}