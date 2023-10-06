package com.example.notesapp.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.notesapp.room.note.Note
import com.example.notesapp.room.note.NoteDao
import com.example.notesapp.room.tag.Tag
import com.example.notesapp.room.tag.TagDao
import kotlinx.coroutines.CoroutineScope

@Database(entities = [Note::class, Tag::class], version = 1, exportSchema = false)
abstract class NoteRoomDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun tagDao(): TagDao

    companion object {
        private var INSTANCE: NoteRoomDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): NoteRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteRoomDatabase::class.java,
                    "note_database"
                )
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}