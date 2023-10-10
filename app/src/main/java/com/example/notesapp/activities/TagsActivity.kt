package com.example.notesapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notesapp.R
import com.example.notesapp.adapters.TagListAdapter
import com.example.notesapp.databinding.ActivityTagsBinding
import com.example.notesapp.fragments.TagDialogModalFragment
import com.example.notesapp.room.NotesApplication
import com.example.notesapp.room.tag.Tag
import com.example.notesapp.viewmodels.TagViewModel
import com.example.notesapp.viewmodels.TagViewModelFactory

class TagsActivity : AppCompatActivity() {
    private val tagViewModel: TagViewModel by viewModels {
        TagViewModelFactory((application as NotesApplication).tagRepository)
    }
    private lateinit var binding: ActivityTagsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tags)

        val recyclerView = binding.tagsRecyclerView
        val adapter = TagListAdapter()

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        tagViewModel.allTags.observe(this) {
            it?.let {
                adapter.submitList(it)
            }
        }

        adapter.onItemClick = { tag: Tag, itemPosition: Int ->
            tagViewModel.tag.value = tag
            val tagModal = TagDialogModalFragment()
            tagModal.show(supportFragmentManager, TagDialogModalFragment.TAG_DIALOG_FRAGMENT_TAG)
        }

        binding.backBtn.setOnClickListener { finish() }
        binding.addTagBtn.setOnClickListener {
            val tagModal = TagDialogModalFragment()
            tagModal.show(supportFragmentManager, TagDialogModalFragment.TAG_DIALOG_FRAGMENT_TAG)
        }
    }
}