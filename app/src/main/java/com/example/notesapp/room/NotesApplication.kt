package com.example.notesapp.room

import android.app.Application
import com.example.notesapp.room.note.NoteRepository
import com.example.notesapp.room.tag.TagRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class NotesApplication : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())

    private val database by lazy { NoteRoomDatabase.getDatabase(this, applicationScope) }
    val noteRepository by lazy { NoteRepository(database.noteDao()) }
    val tagRepository by lazy { TagRepository(database.tagDao()) }
}