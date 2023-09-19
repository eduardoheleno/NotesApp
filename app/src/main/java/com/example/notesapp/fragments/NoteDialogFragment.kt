package com.example.notesapp.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.notesapp.R
import com.example.notesapp.databinding.FragmentNoteDialogBinding
import com.example.notesapp.viewmodels.NoteViewModel

class NoteDialogFragment : DialogFragment() {
    private val noteViewModel: NoteViewModel by activityViewModels()
    private lateinit var binding: FragmentNoteDialogBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setView(R.layout.fragment_note_dialog)
            .create()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.let { dialog ->
            dialog.setCanceledOnTouchOutside(false)
            dialog.window?.let { window ->
                window.attributes.windowAnimations = R.style.DialogAnimation
                window.setGravity(Gravity.BOTTOM)
                window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                window.attributes.y = 100
            }
        }

        println(noteViewModel.currentTitle)

        binding = FragmentNoteDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        const val NOTE_DIALOG_FRAGMENT_TAG = "NOTE_DIALOG_FRAGMENT"
    }
}