package com.example.notesapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.notesapp.room.Note
import com.example.notesapp.room.NoteRepository
import kotlinx.coroutines.launch

class NoteViewModel(private val repository: NoteRepository) : ViewModel() {
    private val _currentNote = MutableLiveData<Note?>(null)
    private val _currentTitle = MutableLiveData<String>()
    private val _currentContent = MutableLiveData<String>()
    val currentNote: LiveData<Note?>
        get() = _currentNote
    val currentTitle: LiveData<String>
        get() = _currentTitle
    val currentContent: LiveData<String>
        get() = _currentContent
    fun insert(note: Note) = viewModelScope.launch {
        repository.insertOrUpdate(note)
    }

    fun setEmptyFields() {
        _currentNote.value = null
        _currentTitle.value = "peido"
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