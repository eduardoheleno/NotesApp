package com.example.notesapp.room.tag

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class TagRepository(private val tagDao: TagDao) {
    fun getAllTags(): Flow<List<Tag>> {
        return tagDao.getAllTags()
    }

    fun getFilteredTags(searchParam: String): Flow<List<Tag>> {
        return tagDao.getFilteredTags(searchParam)
    }

    @WorkerThread
    suspend fun insertOrUpdate(tag: Tag) {
        tagDao.insertOrUpdate(tag)
    }

    @WorkerThread
    suspend fun delete(tag: Tag) {
        tagDao.delete(tag)
    }
}