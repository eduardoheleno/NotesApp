package com.example.notesapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.notesapp.room.note.Note
import com.example.notesapp.room.note.NoteRepository
import com.example.notesapp.room.tag.Tag
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class NoteViewModel(private val repository: NoteRepository) : ViewModel() {
    val searchParam = MutableStateFlow("")

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val filteredNotes: LiveData<List<Note>> = searchParam
        .debounce(300)
        .flatMapLatest {
            if (it.isNotBlank()) {
                repository.getFilteredNotes(it)
            } else {
                repository.getAllNotes()
            }
        }
        .asLiveData()

    private val _currentNote = MutableLiveData<Note?>()
    val currentNoteTitle = MutableLiveData<String>()
    val currentNoteContent = MutableLiveData<String>()
    val currentNoteTag = MutableLiveData<Tag?>()
    val tagId = MutableLiveData<Int>()

    private val _isEditingNoteTitle = MutableLiveData<Boolean>()
    private val _isEditingNoteContent = MutableLiveData<Boolean>()

    private val _isOnSelectMode = MutableLiveData<Boolean>()

    val currentNote: LiveData<Note?>
        get() = _currentNote
    val isEditingNoteTitle: LiveData<Boolean>
        get() = _isEditingNoteTitle
    val isEditingNoteContent: LiveData<Boolean>
        get() = _isEditingNoteContent
    val isOnSelectMode: LiveData<Boolean>
        get() = _isOnSelectMode

    init {
        _currentNote.value = null
        _isEditingNoteTitle.value = false
        _isEditingNoteContent.value = false

        _isOnSelectMode.value = false
    }

    fun setEditingNewNote() {
        _currentNote.value = null
        currentNoteTitle.value = ""
        currentNoteContent.value = ""
        currentNoteTag.value = null

        _isEditingNoteTitle.value = true
        _isEditingNoteContent.value = true
    }

    fun setOpenExistingNote(note: Note) {
        _currentNote.value = note
        currentNoteTitle.value = note.title
        currentNoteContent.value = note.content

        _isEditingNoteTitle.value = false
        _isEditingNoteContent.value = false
    }

    fun setIsOnSelectMode() {
        _isOnSelectMode.value = true
    }

    fun checkIfStillOnSelectMode() {
        val selectedNotes = filteredNotes.value?.filter { it.selected }
        if (selectedNotes?.isEmpty() == true) {
            _isOnSelectMode.value = false
        }
    }

    fun exitSelectMode() {
        filteredNotes.value?.forEach {
            it.selected = false
        }

        _isOnSelectMode.value = false
    }

    fun insert(note: Note) = viewModelScope.launch {
        repository.insertOrUpdate(note)
    }

    fun getSelectedNoteIds(): List<Int>? {
        filteredNotes.value?.let { notes ->
            return notes.filter { it.selected }.map { note -> note.id }
        }

        return null
    }

    fun deleteMultipleNotesById(ids: List<Int>) = viewModelScope.launch {
        repository.deleteListOfNotesIds(ids)
        _isOnSelectMode.value = false
    }

    fun delete(note: Note) = viewModelScope.launch {
        repository.delete(note)
    }

//    Debug functions
    fun insertMultipleRegistersDebug() = viewModelScope.launch {
        repository.insertMultipleDebugRegisters()
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