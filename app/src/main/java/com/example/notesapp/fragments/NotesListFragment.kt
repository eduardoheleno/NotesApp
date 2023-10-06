package com.example.notesapp.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notesapp.adapters.NoteListAdapter
import com.example.notesapp.databinding.FragmentNotesListBinding
import com.example.notesapp.room.note.Note
import com.example.notesapp.room.NotesApplication
import com.example.notesapp.viewmodels.NoteViewModel
import com.example.notesapp.viewmodels.NoteViewModelFactory

/**
 * A simple [Fragment] subclass.
 * Use the [NotesListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NotesListFragment : Fragment() {
    private val noteViewModel: NoteViewModel by activityViewModels {
        NoteViewModelFactory((activity?.application as NotesApplication).noteRepository)
    }
    private lateinit var binding: FragmentNotesListBinding
    private lateinit var adapter: NoteListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotesListBinding.inflate(inflater, container, false)

        observeSelectMode()
        initAdapter()
        initClickListeners()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            @SuppressLint("NotifyDataSetChanged")
            override fun handleOnBackPressed() {
                if (noteViewModel.isOnSelectMode.value == true) {
                    noteViewModel.exitSelectMode()
                    adapter.notifyDataSetChanged()
                } else {
                    requireActivity().finish()
                }
            }
        })

        return binding.root
    }

    private fun initClickListeners() {
        binding.addNewNoteBtn.setOnClickListener {
            val dialog = NoteDialogFragment()
            dialog.show(requireActivity().supportFragmentManager, NoteDialogFragment.NOTE_DIALOG_FRAGMENT_TAG)

            noteViewModel.setEditingNewNote()
        }

        binding.removeNoteBtn.setOnClickListener {
            noteViewModel.getSelectedNoteIds()?.let {
                noteViewModel.deleteMultipleNotesById(it)
            }
        }

        binding.debugButton.setOnClickListener {
            noteViewModel.insertMultipleRegistersDebug()
        }
    }

    private fun initAdapter() {
        val recyclerView = binding.notesRecyclerView
        adapter = NoteListAdapter()

        recyclerView.adapter = adapter
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        noteViewModel.filteredNotes.observe(viewLifecycleOwner) {
            it?.let {
                adapter.submitList(it)
            }
        }

        adapter.onLongItemClick = { note: Note, itemPosition: Int ->
            note.selected = true
            noteViewModel.setIsOnSelectMode()
            adapter.notifyItemChanged(itemPosition)
        }

        adapter.onItemClick = { note: Note, itemPosition: Int ->
            if (noteViewModel.isOnSelectMode.value == true && !note.selected) {
                note.selected = true
                adapter.notifyItemChanged(itemPosition)
            } else if (noteViewModel.isOnSelectMode.value == true && note.selected) {
                note.selected = false
                noteViewModel.checkIfStillOnSelectMode()
                adapter.notifyItemChanged(itemPosition)
            } else {
                val dialog = NoteDialogFragment()
                dialog.show(requireActivity().supportFragmentManager, NoteDialogFragment.NOTE_DIALOG_FRAGMENT_TAG)

                noteViewModel.setOpenExistingNote(note)
            }
        }
    }

    private fun observeSelectMode() {
        noteViewModel.isOnSelectMode.observe(viewLifecycleOwner) {
            if (it) {
                binding.addNewNoteBtn.hide()
                binding.removeNoteBtn.show()
            } else {
                binding.addNewNoteBtn.show()
                binding.removeNoteBtn.hide()
            }
        }
    }
}