package com.ferrao.app.screens.notes

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.ferrao.app.database.Notes
import com.ferrao.app.database.NotesDatabaseDao
import kotlinx.coroutines.*

class NotesViewModel(
    private val database: NotesDatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    var notesList = database.getAllNotes()

    fun insertNote(notes: Notes) {
        uiScope.launch {
            insert(notes)
            notesList = database.getAllNotes()
        }
    }

    fun updateNotes(notes: Notes) {
        uiScope.launch {
            update(notes)
            notesList = database.getAllNotes()
        }
    }

    private suspend fun insert(notes: Notes) {
        withContext(Dispatchers.IO) {
            database.insert(notes)
        }
    }


    private suspend fun update(notes: Notes) {
        withContext(Dispatchers.IO) {
            database.update(notes)
        }
    }


    init {
        Log.i("NotesFragment.kt", "Created")
        //notesList.value
        //populateList()
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("NotesFragment.kt", "Destroyed")
    }

    fun populateList() {
        insertNote(Notes(notesTitle = "Title", notesDesc = "Desc"))
        insertNote(Notes(notesTitle = "Title2", notesDesc = "Desc"))
        //notesList = database.getAllNotes()
        // _notes.value = list
    }

    fun onClear() {
        uiScope.launch {
            clear()
            notesList = database.getAllNotes()
        }
    }

    suspend fun clear() {
        withContext(Dispatchers.IO) {
            database.clear()
        }
    }

    fun onDelete(id: Long) {
        uiScope.launch {
            delete(id)
            notesList = database.getAllNotes()
        }
    }

    private suspend fun delete(id: Long) {
        withContext(Dispatchers.IO) {
            database.deleteNote(id)
        }
    }

}