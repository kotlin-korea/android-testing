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

import java.util.*
import kotlin.jvm.internal.Intrinsics

/**
 * Immutable model class for a Note.
 */
data class Note @JvmOverloads constructor(
        val title: String?,
        val description: String?,
        val imageUrl: String? = null) {

    val id: String = UUID.randomUUID().toString()

    val isEmpty: Boolean
        get() = (title == null || "" == title) && (description == null || "" == description)

    /* todo: 데이터 클래스의 equal, hashCode 동작 확실히 알기
     - equal 값이 같은지 비교, 다른 객체라도 값이 같으면 true
     */
}
