package com.example.notesapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.notesapp.room.tag.Tag
import com.example.notesapp.room.tag.TagRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class TagViewModel(private val tagRepository: TagRepository) : ViewModel() {
    val searchParam = MutableStateFlow("")

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val filteredTags: LiveData<List<Tag>> = searchParam
        .debounce(300)
        .flatMapLatest {
            if (it.isNotBlank()) {
                tagRepository.getFilteredTags(it)
            } else {
                tagRepository.getAllTags()
            }
        }
        .asLiveData()

    val allTags: LiveData<List<Tag>> = tagRepository.getAllTags().asLiveData()

    val tag = MutableLiveData<Tag?>()
    val tagLabel = MutableLiveData<String>()
    val tagColor = MutableLiveData<String>()

    fun saveTag(tag: Tag?) = viewModelScope.launch {
        if (tag != null) {
            tagRepository.insertOrUpdate(tag)
            return@launch
        }

        if (!tagLabel.value.isNullOrBlank()) {
            var color = tagColor.value
            if (color.isNullOrEmpty()) {
                color = Tag.Colors.BLUE.colorCode
            }

            val newTag = Tag(tagLabel.value.toString(), color.toString())
            tagRepository.insertOrUpdate(newTag)

            return@launch
        }
    }

    fun deleteTag(tag: Tag) = viewModelScope.launch {
        tagRepository.delete(tag)
    }
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