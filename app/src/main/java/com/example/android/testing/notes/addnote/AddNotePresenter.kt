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

package com.example.android.testing.notes.addnote

import com.example.android.testing.notes.data.Note
import com.example.android.testing.notes.data.NotesRepository
import com.example.android.testing.notes.util.ImageFile
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Listens to user actions from the UI ([AddNoteFragment]), retrieves the data and updates
 * the UI as required.
 */
class AddNotePresenter(val mNotesRepository: NotesRepository,
                       val mAddNoteView: AddNoteContract.View,
                       private val mImageFile: ImageFile) : AddNoteContract.UserActionsListener {

    override fun saveNote(title: String, description: String) {
        var imageUrl: String? = null
        if (mImageFile.exists()) {
            imageUrl = mImageFile.path
        }
        val newNote = Note(title, description, imageUrl)
        if (newNote.isEmpty) {
            mAddNoteView.showEmptyNoteError()
        }
        else {
            mNotesRepository.saveNote(newNote)
            mAddNoteView.showNotesList()
        }
    }

    @Throws(IOException::class)
    override fun takePicture() {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        mImageFile.create(imageFileName, ".jpg")
        mAddNoteView.openCamera(mImageFile.path)
    }

    override fun imageAvailable() {
        if (mImageFile.exists()) {
            mAddNoteView.showImagePreview(mImageFile.path)
        }
        else {
            imageCaptureFailed()
        }
    }

    override fun imageCaptureFailed() {
        captureFailed()
    }

    private fun captureFailed() {
        mImageFile.delete()
        mAddNoteView.showImageError()
    }

}
