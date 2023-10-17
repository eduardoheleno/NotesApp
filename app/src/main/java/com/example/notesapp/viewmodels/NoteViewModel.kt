package com.example.notesapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.notesapp.room.NoteWithTag
import com.example.notesapp.room.note.Note
import com.example.notesapp.room.note.NoteRepository
import com.example.notesapp.room.tag.Tag
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class NoteViewModel(private val repository: NoteRepository) : ViewModel() {
    val searchParam = MutableStateFlow("")
    val tagIdParam = MutableStateFlow<Int?>(null)

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val filteredNotes: LiveData<List<NoteWithTag>> = combine(searchParam, tagIdParam) { search: String, tagId: Int? ->
        Pair(search, tagId)
    }
        .debounce(300)
        .flatMapLatest { (search, tagId) ->
            if (search.isNotBlank() && tagId != null) {
                repository.getFilteredNotes(search, tagId)
            } else if (search.isNotBlank()) {
                repository.getFilteredNotes(search, null)
            } else if (tagId != null) {
                repository.getFilteredNotes(null, tagId)
            } else {
                repository.getAllNotes()
            }
        }
        .asLiveData()

    private val _currentNote = MutableLiveData<NoteWithTag?>()
    val currentNoteTitle = MutableLiveData<String>()
    val currentNoteContent = MutableLiveData<String>()
    val currentNoteTag = MutableLiveData<Tag?>()
    val tagId = MutableLiveData<Int?>()

    private val _isOnSelectMode = MutableLiveData<Boolean>()

    val currentNote: LiveData<NoteWithTag?>
        get() = _currentNote
    val isOnSelectMode: LiveData<Boolean>
        get() = _isOnSelectMode

    init {
        _currentNote.value = null
        _isOnSelectMode.value = false
    }

    fun setEditingNewNote() {
        _currentNote.value = null
        currentNoteTitle.value = ""
        currentNoteContent.value = ""
        currentNoteTag.value = null
    }

    fun setOpenExistingNote(noteWithTag: NoteWithTag) {
        _currentNote.value = noteWithTag
        currentNoteTitle.value = noteWithTag.note.title
        currentNoteContent.value = noteWithTag.note.content
        currentNoteTag.value = noteWithTag.tag
    }

    fun setIsOnSelectMode() {
        _isOnSelectMode.value = true
    }

    fun checkIfStillOnSelectMode() {
        val selectedNotes = filteredNotes.value?.filter { it.note.selected }
        if (selectedNotes?.isEmpty() == true) {
            _isOnSelectMode.value = false
        }
    }

    fun exitSelectMode() {
        filteredNotes.value?.forEach {
            it.note.selected = false
        }

        _isOnSelectMode.value = false
    }

    fun insertOrUpdate(note: Note) = viewModelScope.launch {
        repository.insertOrUpdate(note)
    }

    fun getSelectedNoteIds(): List<Int>? {
        filteredNotes.value?.let {
            return it.filter { list -> list.note.selected }.map { selectedList -> selectedList.note.id }
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