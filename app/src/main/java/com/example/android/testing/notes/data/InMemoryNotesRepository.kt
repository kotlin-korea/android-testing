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

import com.google.common.collect.ImmutableList
import android.support.annotation.VisibleForTesting

import com.google.common.base.Preconditions.checkNotNull

/**
 * Concrete implementation to load notes from the a data source.
 */
class InMemoryNotesRepository(val mNotesServiceApi: NotesServiceApi) : NotesRepository {

    /**
     * This method has reduced visibility for testing and is only visible to tests in the same
     * package.
     */
    @VisibleForTesting
    internal var mCachedNotes: List<Note>? = null
    /* todo: internal 키워드에 대해서 알아보기
     - byte code 를 디컴파일 해보면 private 이다
     - 같은 모듈안에서 접근이 가능한 모디파이어
     - 라이브러리 만들때 쓰면 좋은건가 ?

     */

    override fun getNotes(callback: NotesRepository.LoadNotesCallback) {
        // Load from API only if needed.
        if (mCachedNotes == null) {
            mNotesServiceApi.getAllNotes(object : NotesServiceApi.NotesServiceCallback<List<Note>> {
                override fun onLoaded(notes: List<Note>) {
                    mCachedNotes = ImmutableList.copyOf(notes)
                    callback.onNotesLoaded(mCachedNotes)
                }
            })
        }
        else {
            callback.onNotesLoaded(mCachedNotes)
        }
    }

    override fun saveNote(note: Note) {
        checkNotNull(note)
        mNotesServiceApi.saveNote(note)
        refreshData()
    }

    override fun getNote(noteId: String, callback: NotesRepository.GetNoteCallback) {
        // Load notes matching the id always directly from the API.
        mNotesServiceApi.getNote(noteId, object : NotesServiceApi.NotesServiceCallback<Note?> {
            override fun onLoaded(note: Note?) {
                callback.onNoteLoaded(note)
            }
        })
    }

    override fun refreshData() {
        mCachedNotes = null
    }

}
