package com.example.notesapp.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.notesapp.R
import com.example.notesapp.databinding.FragmentNoteDialogBinding
import com.example.notesapp.room.Note
import com.example.notesapp.room.NotesApplication
import com.example.notesapp.viewmodels.NoteViewModel
import com.example.notesapp.viewmodels.NoteViewModelFactory

class NoteDialogFragment : DialogFragment() {
    private val noteViewModel: NoteViewModel by activityViewModels {
        NoteViewModelFactory((activity?.application as NotesApplication).repository)
    }
    private lateinit var binding: FragmentNoteDialogBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = FragmentNoteDialogBinding.inflate(inflater)

        binding.noteViewModel = noteViewModel
        binding.lifecycleOwner = this

        val dialog = AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()

        dialog.window?.let {
            it.attributes.windowAnimations = R.style.DialogAnimation
            it.setGravity(Gravity.BOTTOM)
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            it.attributes.y = 100
        }

        return dialog
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        val noteTitle = binding.editNoteTitleText.text.toString()
        val noteContent = binding.editNoteContentText.text.toString()

        if (noteTitle.isNotEmpty() && noteContent.isNotEmpty() && noteViewModel.currentNote.value == null) {
            val note = Note(noteTitle, noteContent, Note.Colors.values().random().colorCode)
            noteViewModel.insert(note)
        }
    }

    companion object {
        const val NOTE_DIALOG_FRAGMENT_TAG = "NOTE_DIALOG_FRAGMENT"
    }
}