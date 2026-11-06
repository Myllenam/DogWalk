package com.example.dogwalk.data.repository

import com.example.dogwalk.data.local.dao.WalkDao
import com.example.dogwalk.model.Walk
import kotlinx.coroutines.flow.Flow

class WalkRepository(private val walkDao: WalkDao) {

    fun getWalks(): Flow<List<Walk>> = walkDao.getAllWalks()

    fun getWalk(id: Long): Flow<Walk?> = walkDao.getWalkById(id)

    suspend fun saveWalk(walk: Walk): Long = walkDao.insertWalk(walk)
}
