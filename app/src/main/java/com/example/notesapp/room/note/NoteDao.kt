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

    @Query("SELECT * FROM note WHERE (:searchParam IS NULL OR (title LIKE '%' || :searchParam || '%' OR content LIKE '%' || :searchParam || '%')) AND (:tagId IS NULL OR tagId = :tagId) ORDER BY id DESC")
    fun getFilteredNotes(searchParam: String?, tagId: Int?): Flow<List<Note>>

    @Upsert
    suspend fun insertOrUpdate(note: Note)

    @Query("DELETE FROM note WHERE id IN(:idList)")
    suspend fun deleteListOfNotesIds(idList: List<Int>)

    @Delete
    suspend fun delete(note: Note)
}