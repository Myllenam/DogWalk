package com.example.dogwalk.ui.walk

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogwalk.data.local.database.DogWalkDatabase
import com.example.dogwalk.model.Walk
import com.example.dogwalk.data.repository.WalkRepository
import com.example.dogwalk.data.repository.PetRepository
import kotlinx.coroutines.launch

class WalkViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = WalkRepository(
        DogWalkDatabase.getDatabase(application).walkDao()
    )
    private val petRepository = PetRepository(
        DogWalkDatabase.getDatabase(application).petDao()
    )

    fun finishWalk(
        petId: Long,
        startedAt: Long,
        durationSeconds: Long,
        distanceKm: Double,
        steps: Int,
        onSaved: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            val pet = petRepository.getPetById(petId)
            if (pet == null) {
                onError("Não foi possível encontrar o pet selecionado")
                return@launch
            }
            repository.saveWalk(
                Walk(
                    id = 0,
                    petId = pet.id,
                    petName = pet.name,
                    startedAt = startedAt,
                    durationMinutes = (durationSeconds / 60L).toInt(),
                    distanceKm = distanceKm,
                    steps = steps,
                    durationSeconds = durationSeconds
                )
            )
            onSaved()
        }
    }
}
