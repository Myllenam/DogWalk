package com.example.dogwalk.ui.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.dogwalk.data.local.database.DogWalkDatabase
import com.example.dogwalk.model.Walk
import com.example.dogwalk.data.repository.WalkRepository
import com.example.dogwalk.data.repository.PetRepository

class HistoryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = WalkRepository(
        DogWalkDatabase.getDatabase(application).walkDao()
    )
    private val petRepository = PetRepository(
        DogWalkDatabase.getDatabase(application).petDao()
    )

    val walks: LiveData<List<Walk>> = repository.getWalks().asLiveData()
    val pets = petRepository.getPets().asLiveData()
}
