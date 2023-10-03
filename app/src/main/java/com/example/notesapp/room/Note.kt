package com.example.notesapp.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class Note(
    @ColumnInfo val title: String,
    @ColumnInfo val content: String,
    @ColumnInfo val color: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
) {
    @Ignore var selected: Boolean = false

    enum class Colors(val colorCode: String) {
        BLUE("#D9E8FC"),
        YELLOW("#FDE99D"),
        ORANGE("#FFEADD"),
        PINK("#FFD8F4"),
        GREEN("#B0E9CA")
    }
}