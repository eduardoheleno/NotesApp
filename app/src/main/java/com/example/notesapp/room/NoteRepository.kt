package com.example.notesapp.room

import androidx.annotation.WorkerThread
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.Flow

class NoteRepository(private val noteDao: NoteDao) {
   val allNotes: Flow<List<Note>>  = noteDao.getAllWords()

    @WorkerThread
    suspend fun insertOrUpdate(note: Note) {
        noteDao.insertOrUpdate(note)
    }
}