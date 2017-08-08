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

package com.example.android.testing.notes.data

import android.os.Handler
import android.support.v4.util.ArrayMap

import java.util.ArrayList

/**
 * Implementation of the Notes Service API that adds a latency simulating network.
 */
class NotesServiceApiImpl : NotesServiceApi {

    override fun getAllNotes(callback: NotesServiceApi.NotesServiceCallback<List<Note>>) {
        // Simulate network by delaying the execution.
        val handler = Handler()
        handler.postDelayed({
            val notes = ArrayList(NOTES_SERVICE_DATA.values)
            callback.onLoaded(notes)
        }, SERVICE_LATENCY_IN_MILLIS.toLong())
    }

    override fun getNote(noteId: String, callback: NotesServiceApi.NotesServiceCallback<Note?>) {
        //TODO: Add network latency here too.
        val note = NOTES_SERVICE_DATA[noteId]
        callback.onLoaded(note)
    }

    override fun saveNote(note: Note) {
        NOTES_SERVICE_DATA.put(note.id, note)
    }

    companion object {

        private val SERVICE_LATENCY_IN_MILLIS = 2000
        private val NOTES_SERVICE_DATA = NotesServiceApiEndpoint.loadPersistedNotes()
    }

}
