package com.example.notesapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.notesapp.R
import com.example.notesapp.databinding.ActivityTagsBinding
import com.example.notesapp.room.NotesApplication
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

        binding.backBtn.setOnClickListener { finish() }
    }
}