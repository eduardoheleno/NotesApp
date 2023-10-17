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
import com.example.notesapp.room.note.Note
import com.example.notesapp.room.NotesApplication
import com.example.notesapp.viewmodels.NoteViewModel
import com.example.notesapp.viewmodels.NoteViewModelFactory

class NoteDialogFragment : DialogFragment() {
    private val noteViewModel: NoteViewModel by activityViewModels {
        NoteViewModelFactory((activity?.application as NotesApplication).noteRepository)
    }
    private lateinit var binding: FragmentNoteDialogBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = FragmentNoteDialogBinding.inflate(inflater)

        binding.noteViewModel = noteViewModel
        binding.lifecycleOwner = this

        binding.tagSearchBtn.setOnClickListener {
            TagSearchDialogFragment().show(requireActivity().supportFragmentManager, TagSearchDialogFragment.TAG_SEARCH_DIALOG_FRAGMENT)
        }

        binding.removeTagBtn.setOnClickListener { noteViewModel.currentNoteTag.value = null }

        binding.tagLabelSelected.text = noteViewModel.currentNote.value?.tag?.label

        noteViewModel.currentNoteTag.observe(requireActivity()) {
            if (it != null) {
                noteViewModel.showRemoveTagBtn.value = true

                noteViewModel.tagId.value = it.id
                binding.tagLabelSelected.text = it.label
            } else {
                noteViewModel.showRemoveTagBtn.value = false

                noteViewModel.tagId.value = null
                binding.tagLabelSelected.text = ""
            }
        }

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
        val noteTitle = binding.editNoteTitleText.text.toString()
        val noteContent = binding.editNoteContentText.text.toString()
        val noteTagId = noteViewModel.tagId.value

        if (noteViewModel.currentNote.value != null) {
            val note = Note(noteTitle, noteContent, noteTagId, noteViewModel.currentNote.value!!.note.id)
            noteViewModel.insertOrUpdate(note)
        } else if (noteTitle.isNotBlank() && noteContent.isNotBlank()) {
            val note = Note(noteTitle, noteContent, noteTagId)
            noteViewModel.insertOrUpdate(note)
        }

        super.onDismiss(dialog)
    }

    companion object {
        const val NOTE_DIALOG_FRAGMENT_TAG = "NOTE_DIALOG_FRAGMENT"
    }
}