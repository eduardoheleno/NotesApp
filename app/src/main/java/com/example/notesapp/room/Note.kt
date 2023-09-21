package com.example.notesapp.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class Note(
    @ColumnInfo val title: String,
    @ColumnInfo val content: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
) {
    @Ignore var selected: Boolean = false
}