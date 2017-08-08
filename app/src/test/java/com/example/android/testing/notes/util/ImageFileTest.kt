/*
 * Copyright (C) 2015 The Android Open Source Project
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


import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

import android.os.Environment

import java.io.File
import java.io.IOException

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.CoreMatchers.nullValue
import org.junit.Assert.assertThat
import org.mockito.Matchers.anyString
import org.mockito.Matchers.eq
import org.powermock.api.mockito.PowerMockito.mockStatic
import org.powermock.api.mockito.PowerMockito.`when`


/**
 * Unit tests for the implementation of [ImageFileImpl].
 *
 *
 * The current Android tools support for writing unit tests is limited and requires mocking of all
 * Android dependencies in unit tests. That's why unit tests ideally should not have any
 * dependencies into android.jar, but sometimes they are inevitable. Usually using a wrapper class
 * or using a mocking framework like Mockito works fine, but there are situations where these
 * frameworks fall short, for instance when working with static util classes in the android.jar.
 *
 *
 *
 *
 * To work around that limitation this test uses Powermockito, a library which adds support for
 * mocking static methods to Mockito. Powermockito should be used with care since it is normally a
 * sign of a bad code design. Nevertheless it can be handy while working with third party
 * dependencies, like the android.jar.
 */
@RunWith(PowerMockRunner::class)
@PrepareForTest(Environment::class, File::class) // Prepare the static classes for mocking
class ImageFileTest {

    @Mock
    private val mDirectory: File? = null

    @Mock
    private val mImageFile: File? = null

    private var mFileHelper: ImageFileImpl? = null

    @Before
    @Throws(IOException::class)
    fun createImageFile() {
        // Get a reference to the class under test
        mFileHelper = ImageFileImpl()

        // Setup required static mocking
        withStaticallyMockedEnvironmentAndFileApis()
    }

    @Test
    @Throws(IOException::class)
    fun create_SetsImageFile() {
        // When file helper is asked to create a file
        mFileHelper!!.create("Name", "Extension")

        // Then the created file is stored inside the image file.
        assertThat<File>(mFileHelper!!.mImageFile, `is`(notNullValue<Any>()))
    }

    @Test
    fun deleteImageFile() {
        // When file should be deleted
        mFileHelper!!.delete()

        // Then stored file is deleted
        assertThat<File>(mFileHelper!!.mImageFile, `is`(nullValue<Any>()))
    }

    /**
     * Mock static methods in android.jar
     */
    @Throws(IOException::class)
    private fun withStaticallyMockedEnvironmentAndFileApis() {
        // Setup mocking for Environment and File classes
        mockStatic(Environment::class.java, File::class.java)

        // Make the Environment class return a mocked external storage directory
        `when`(Environment.getExternalStorageDirectory())
                .thenReturn(mDirectory)

        // Make the File class return a mocked image file
        `when`(File.createTempFile(anyString(), anyString(), eq<File>(mDirectory))).thenReturn(mImageFile)
    }
}
