package com.example.notesapp.room.note

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("SELECT * FROM note ORDER BY id DESC")
    fun getAllNotes(): Flow<List<Note>>

    @Query("SELECT * FROM note WHERE title LIKE '%' || :searchParam || '%' OR content LIKE '%' || :searchParam || '%' ORDER BY id DESC")
    fun getFilteredNotes(searchParam: String): Flow<List<Note>>

    @Upsert
    suspend fun insertOrUpdate(note: Note)

    @Query("DELETE FROM note WHERE id IN(:idList)")
    suspend fun deleteListOfNotesIds(idList: List<Int>)

    @Delete
    suspend fun delete(note: Note)
}