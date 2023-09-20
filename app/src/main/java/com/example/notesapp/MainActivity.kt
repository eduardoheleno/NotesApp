package com.example.notesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.notesapp.databinding.ActivityMainBinding
import com.example.notesapp.fragments.NoteDialogFragment
import com.example.notesapp.room.NotesApplication
import com.example.notesapp.viewmodels.NoteViewModel
import com.example.notesapp.viewmodels.NoteViewModelFactory

class MainActivity : AppCompatActivity() {
    private val noteViewModel: NoteViewModel by viewModels {
        NoteViewModelFactory((application as NotesApplication).repository)
    }

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.addNewNoteBtn.setOnClickListener {
            val dialog = NoteDialogFragment()
            dialog.show(supportFragmentManager, NoteDialogFragment.NOTE_DIALOG_FRAGMENT_TAG)

            noteViewModel.setEditingNewNote()
        }
    }
}