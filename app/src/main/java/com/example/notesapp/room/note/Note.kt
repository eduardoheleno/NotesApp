package com.example.notesapp.room.note

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.notesapp.room.tag.Tag

@Entity(
    tableName = "Note",
    foreignKeys = [
        ForeignKey(
            entity = Tag::class,
            parentColumns = ["id"],
            childColumns = ["tagId"],
            onDelete = ForeignKey.SET_NULL,
            onUpdate = ForeignKey.NO_ACTION
        )
    ]
)
data class Note(
    @ColumnInfo val title: String,
    @ColumnInfo val content: String,
    @ColumnInfo(index = true) val tagId: Int?,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
) {
    @Ignore var selected: Boolean = false
}