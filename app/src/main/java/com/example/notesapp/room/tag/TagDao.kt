package com.example.notesapp.room.tag

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface TagDao {
    @Query("SELECT * FROM tag ORDER BY id DESC")
    fun getAllTags(): Flow<List<Tag>>

    @Query("SELECT * FROM tag WHERE label LIKE '%' || :searchParam || '%' ORDER BY id DESC")
    fun getFilteredTags(searchParam: String): Flow<List<Tag>>

    @Upsert
    suspend fun insertOrUpdate(tag: Tag)

    @Delete
    suspend fun delete(tag: Tag)
}