package com.example.dogwalk.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.dogwalk.model.Pet
import kotlinx.coroutines.flow.Flow

@Dao
interface PetDao {

    @Query("SELECT * FROM pets ORDER BY name ASC")
    fun getAllPets(): Flow<List<Pet>>

    @Query("SELECT * FROM pets WHERE id = :id LIMIT 1")
    suspend fun getPetById(id: Long): Pet?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePet(pet: Pet): Long

    @Delete
    suspend fun deletePet(pet: Pet)
}