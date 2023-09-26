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

    @WorkerThread
    suspend fun deleteListOfNotesIds(idList: List<Int>) {
        noteDao.deleteListOfNotesIds(idList)
    }

    @WorkerThread
    suspend fun delete(note: Note) {
        noteDao.delete(note)
    }
}