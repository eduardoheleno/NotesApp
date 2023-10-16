package com.example.notesapp.room

import androidx.room.Embedded
import androidx.room.Relation
import com.example.notesapp.room.note.Note
import com.example.notesapp.room.tag.Tag

data class NoteWithTag(
    @Embedded val note: Note,
    @Relation(
        parentColumn = "tagId",
        entityColumn = "id"
    )
    val tag: Tag? = null
)
