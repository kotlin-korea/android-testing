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

package com.example.android.testing.notes.util

import android.net.Uri
import android.os.Environment
import android.support.annotation.VisibleForTesting

import java.io.File
import java.io.IOException

/**
 * A thin wrapper around Android file APIs to make them more testable and allows the injection of a
 * fake implementation for hermetic UI tests.
 */
open class ImageFileImpl : ImageFile {

    @VisibleForTesting
    var mImageFile: File? = null

    @Throws(IOException::class)
    override fun create(name: String, extension: String) {
        val storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES)

        mImageFile = File.createTempFile(
                name, /* prefix */
                extension, /* suffix */
                storageDir      /* directory */
        )
    }

    override fun exists(): Boolean {
        return null != mImageFile && mImageFile!!.exists()
    }

    override fun delete() {
        mImageFile = null
    }

    override val path: String
        get() = Uri.fromFile(mImageFile).toString()

}
