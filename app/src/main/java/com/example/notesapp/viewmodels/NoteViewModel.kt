package com.example.notesapp.viewmodels

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.notesapp.room.Note
import com.example.notesapp.room.NoteRepository
import kotlinx.coroutines.launch

class NoteViewModel(private val repository: NoteRepository) : ViewModel() {
    private val _currentNote = MutableLiveData<Note?>()
    private val _currentTitle = MutableLiveData<String>()
    private val _currentContent = MutableLiveData<String>()
    private val _noteTitleEditVisibility = MutableLiveData<Int>()
    private val _noteContentEditVisibility = MutableLiveData<Int>()
    val currentNote: LiveData<Note?>
        get() = _currentNote
    val currentTitle: LiveData<String>
        get() = _currentTitle
    val currentContent: LiveData<String>
        get() = _currentContent
    val noteTitleEditVisibility: LiveData<Int>
        get() = _noteTitleEditVisibility
    val noteContentEditVisibility: LiveData<Int>
        get() = _noteContentEditVisibility

    init {
        _noteTitleEditVisibility.value = View.GONE
        _noteContentEditVisibility.value = View.GONE
    }

    fun insert(note: Note) = viewModelScope.launch {
        repository.insertOrUpdate(note)
    }

    fun setEmptyFields() {
        _currentNote.value = null
        _currentTitle.value = ""
        _currentContent.value = ""
    }
}

class NoteViewModelFactory(private val repository: NoteRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NoteViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}