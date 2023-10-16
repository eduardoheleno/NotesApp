package com.example.notesapp.room.note

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class NoteRepository(private val noteDao: NoteDao) {
    fun getAllNotes(): Flow<List<Note>> {
        return noteDao.getAllNotes()
    }

    fun getFilteredNotes(searchParam: String?, tagId: Int?): Flow<List<Note>> {
        return noteDao.getFilteredNotes(searchParam, tagId)
    }

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

//    Debug functions

    suspend fun insertMultipleDebugRegisters() {
        for (i in 1..10) {
            val note = Note("title$i", "content$i", null)
            noteDao.insertOrUpdate(note)
        }
    }
}