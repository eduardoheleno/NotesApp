package com.example.notesapp.room.tag

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Tag(
    @ColumnInfo val label: String,
    @ColumnInfo val color: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
) {
    enum class Colors(val colorCode: String) {
        BLUE("#D9E8FC"),
        YELLOW("#FDE99D"),
        ORANGE("#FFEADD"),
        PINK("#FFD8F4"),
        GREEN("#B0E9CA")
    }
}
