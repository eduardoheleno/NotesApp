package com.example.notesapp.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notesapp.adapters.NoteListAdapter
import com.example.notesapp.databinding.FragmentNotesListBinding
import com.example.notesapp.room.Note
import com.example.notesapp.room.NotesApplication
import com.example.notesapp.viewmodels.NoteViewModel
import com.example.notesapp.viewmodels.NoteViewModelFactory

/**
 * A simple [Fragment] subclass.
 * Use the [NotesListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NotesListFragment : Fragment(), NoteListAdapter.CallbackInterface {
    private val noteViewModel: NoteViewModel by activityViewModels {
        NoteViewModelFactory((activity?.application as NotesApplication).repository)
    }
    private lateinit var binding: FragmentNotesListBinding
    private lateinit var adapter: NoteListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotesListBinding.inflate(inflater, container, false)

        val recyclerView = binding.notesRecyclerView
        adapter = NoteListAdapter(this)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        noteViewModel.allNotes.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

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

    override fun setIsOnSelectMode() {
        noteViewModel.setIsOnSelectMode()
    }

    override fun onClickNote(note: Note, itemPosition: Int) {
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