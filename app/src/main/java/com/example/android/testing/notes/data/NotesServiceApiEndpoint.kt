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

import android.support.v4.util.ArrayMap

/**
 * This is the endpoint for your data source. Typically, it would be a SQLite db and/or a server
 * API. In this example, we fake this by creating the data on the fly.
 */
object NotesServiceApiEndpoint {

    /* todo: DATA 를 위에쓰고 init 에서 초기화를 할수있는데, init 에서 초기화 설정하는데 DATA 변수를 init 밑에서 선언하면 컴파일 에러 */

    private val DATA: ArrayMap<String, Note> = ArrayMap(2)

    init {
        addNote("Oh yes!", "I demand trial by Unit testing", null)
        addNote("Espresso", "UI Testing for Android", null)
    }

    private fun addNote(title: String, description: String, imageUrl: String?) {
        val newNote = Note(title, description, imageUrl)
        DATA.put(newNote.id, newNote)
    }

    /**
     * @return the Notes to show when starting the app.
     */
    fun loadPersistedNotes(): ArrayMap<String, Note> {
        return DATA
    }
}
