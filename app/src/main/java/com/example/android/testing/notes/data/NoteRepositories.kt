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

object NoteRepositories {

    var repository: NotesRepository? = null

    @Synchronized
    fun getInMemoryRepoInstance(notesServiceApi: NotesServiceApi): NotesRepository {
        if (null == repository)
            repository = InMemoryNotesRepository(notesServiceApi)
        return repository!!
    }

    /* todo: kotlin 에서 singleton 패턴 하는법 찾아보기
     https://blog.rahulchowdhury.co/not-so-singletons-in-kotlin/

     - thread safe
     - lazy initialization
        - 자바로 디컴파일된 파일을 보면 lazy 하지 않다.
        - 코틀린은 lazy 하다
     - reflection proof
    */
}