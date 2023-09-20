package com.example.notesapp.room

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("SELECT * FROM note ORDER BY id DESC")
    fun getAllWords(): Flow<List<Note>>
    @Upsert
    suspend fun insertOrUpdate(note: Note)
}