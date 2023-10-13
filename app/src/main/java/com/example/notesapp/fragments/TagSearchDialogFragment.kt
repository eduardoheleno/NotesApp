package com.example.notesapp.fragments

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notesapp.R
import com.example.notesapp.adapters.TagListAdapter
import com.example.notesapp.databinding.FragmentTagSearchDialogBinding
import com.example.notesapp.room.NotesApplication
import com.example.notesapp.room.tag.Tag
import com.example.notesapp.viewmodels.NoteViewModel
import com.example.notesapp.viewmodels.NoteViewModelFactory
import com.example.notesapp.viewmodels.TagViewModel
import com.example.notesapp.viewmodels.TagViewModelFactory

/**
 * A simple [Fragment] subclass.
 * Use the [TagSearchDialogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TagSearchDialogFragment : DialogFragment() {
    private val tagViewModel: TagViewModel by activityViewModels {
        TagViewModelFactory((activity?.application as NotesApplication).tagRepository)
    }
    private val noteViewModel: NoteViewModel by activityViewModels {
        NoteViewModelFactory((activity?.application as NotesApplication).noteRepository)
    }
    private lateinit var binding: FragmentTagSearchDialogBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tag_search_dialog, container, false)
        binding.lifecycleOwner = this
        binding.tagViewModel = tagViewModel

        initRecyclerView()

        return binding.root
    }

    override fun onDismiss(dialog: DialogInterface) {
        tagViewModel.searchParam.value = ""
        super.onDismiss(dialog)
    }

    private fun initRecyclerView() {
        val recyclerView = binding.searchedTagRecyclerView
        val adapter = TagListAdapter()

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        tagViewModel.filteredTags.observe(viewLifecycleOwner) {
            it?.let {
                adapter.submitList(it)
            }
        }

        adapter.onItemClick = { tag: Tag, _ ->
            noteViewModel.currentNoteTag.value = tag
            dismiss()
        }
    }

    companion object {
        const val TAG_SEARCH_DIALOG_FRAGMENT = "TAG_SEARCH_DIALOG_FRAGMENT"
    }
}