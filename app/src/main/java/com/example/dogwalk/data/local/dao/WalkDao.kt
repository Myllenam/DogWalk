package com.example.dogwalk.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.dogwalk.model.Walk
import kotlinx.coroutines.flow.Flow

@Dao
interface WalkDao {

    @Query("SELECT * FROM walks ORDER BY startedAt DESC")
    fun getAllWalks(): Flow<List<Walk>>

    @Query("SELECT * FROM walks WHERE id = :id LIMIT 1")
    fun getWalkById(id: Long): Flow<Walk?>

    @Insert
    suspend fun insertWalk(walk: Walk): Long
}
