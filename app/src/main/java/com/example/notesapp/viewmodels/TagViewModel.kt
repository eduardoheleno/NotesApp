package com.example.notesapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.notesapp.room.tag.Tag
import com.example.notesapp.room.tag.TagRepository

class TagViewModel(private val tagRepository: TagRepository) : ViewModel() {
    val allTags: LiveData<List<Tag>> = tagRepository.getAllTags().asLiveData()

    val tagLabel = MutableLiveData<String>()
    val tagColor = MutableLiveData<String>()
}

class TagViewModelFactory(private val tagRepository: TagRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TagViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TagViewModel(tagRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}