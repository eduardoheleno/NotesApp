package com.example.notesapp.room

import androidx.room.Dao
import androidx.room.Upsert

@Dao
interface NoteDao {
    @Upsert
    suspend fun insertOrUpdate(note: Note)
}