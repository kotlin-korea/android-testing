/*
 * Copyright 2015, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.testing.notes.notes

import com.example.android.testing.notes.data.Note
import com.example.android.testing.notes.data.NotesRepository
import com.example.android.testing.notes.util.EspressoIdlingResource

import com.google.common.base.Preconditions.checkNotNull


/**
 * Listens to user actions from the UI ([NotesFragment]), retrieves the data and updates the
 * UI as required.
 */
class NotesPresenter(
        val mNotesRepository: NotesRepository, val mNotesView: NotesContract.View) : NotesContract.UserActionsListener {

    override fun loadNotes(forceUpdate: Boolean) {
        mNotesView.setProgressIndicator(true)
        if (forceUpdate) {
            mNotesRepository.refreshData()
        }

        // The network request might be handled in a different thread so make sure Espresso knows
        // that the app is busy until the response is handled.
        EspressoIdlingResource.increment() // App is busy until further notice

        mNotesRepository.getNotes(object : NotesRepository.LoadNotesCallback {
            override fun onNotesLoaded(notes: List<Note>?) {
                EspressoIdlingResource.decrement() // Set app as idle.
                mNotesView.setProgressIndicator(false)
                mNotesView.showNotes(notes ?: ArrayList<Note>())
            }
        })
    }

    override fun addNewNote() {
        mNotesView.showAddNote()
    }

    override fun openNoteDetails(requestedNote: Note) {
        checkNotNull(requestedNote, "requestedNote cannot be null!")
        mNotesView.showNoteDetailUi(requestedNote.id)
    }

}
