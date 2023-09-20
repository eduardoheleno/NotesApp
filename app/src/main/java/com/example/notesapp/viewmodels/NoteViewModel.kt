package com.example.notesapp.viewmodels

import androidx.databinding.BaseObservable
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
    val currentNoteTitle = MutableLiveData<String>()
    val currentNoteContent = MutableLiveData<String>()

    private val _isEditingNoteTitle = MutableLiveData<Boolean>()
    private val _isEditingNoteContent = MutableLiveData<Boolean>()

    val currentNote: LiveData<Note?>
        get() = _currentNote
    val isEditingNoteTitle: LiveData<Boolean>
        get() = _isEditingNoteTitle
    val isEditingNoteContent: LiveData<Boolean>
        get() = _isEditingNoteContent

    init {
        _currentNote.value = null
        _isEditingNoteTitle.value = false
        _isEditingNoteContent.value = false
    }

    fun setEditingNewNote() {
        _currentNote.value = null
        currentNoteTitle.value = ""
        currentNoteContent.value = ""

        _isEditingNoteTitle.value = true
        _isEditingNoteContent.value = true
    }

    fun insert(note: Note) = viewModelScope.launch {
        repository.insertOrUpdate(note)
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