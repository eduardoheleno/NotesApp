package com.example.notesapp.room

import androidx.annotation.WorkerThread

class NoteRepository(private val noteDao: NoteDao) {
    @WorkerThread
    suspend fun insertOrUpdate(note: Note) {
        noteDao.insertOrUpdate(note)
    }
}