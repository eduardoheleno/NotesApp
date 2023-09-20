package com.example.notesapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotesListBinding.inflate(inflater, container, false)

        val recyclerView = binding.notesRecyclerView
        val adapter = NoteListAdapter(this)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        noteViewModel.allNotes.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        return binding.root
    }

    override fun openDialogWithNote(note: Note) {
        val dialog = NoteDialogFragment()
        dialog.show(requireActivity().supportFragmentManager, NoteDialogFragment.NOTE_DIALOG_FRAGMENT_TAG)

        noteViewModel.setOpenExistingNote(note)
    }
}