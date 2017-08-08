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

package com.example.android.testing.notes.notedetail

/**
 * This specifies the contract between the view and the presenter.
 */
interface NoteDetailContract {

    interface View {

        fun setProgressIndicator(active: Boolean)

        fun showMissingNote()

        fun hideTitle()

        fun showTitle(title: String)

        fun showImage(imageUrl: String)

        fun hideImage()

        fun hideDescription()

        fun showDescription(description: String)
    }

    interface UserActionsListener {

        fun openNote(noteId: String?)
    }
}
