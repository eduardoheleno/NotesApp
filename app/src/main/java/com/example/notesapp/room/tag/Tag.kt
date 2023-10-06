package com.example.notesapp.room.tag

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Tag(
    @ColumnInfo val label: String,
    @ColumnInfo val color: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)
