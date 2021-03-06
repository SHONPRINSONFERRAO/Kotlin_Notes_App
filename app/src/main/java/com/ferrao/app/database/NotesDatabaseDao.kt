/*
 * Copyright 2019, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ferrao.app.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface NotesDatabaseDao {
    @Insert
    fun insert(notes: Notes)

    @Update
    fun update(notes: Notes)

    @Query("SELECT * from notes_table WHERE notesId = :key")
    fun get(key: Long): Notes?

    @Query("DELETE FROM notes_table")
    fun clear()

    @Query("SELECT * FROM notes_table ORDER BY notesId DESC LIMIT 1")
    fun getNote(): Notes?

    @Query("SELECT * FROM notes_table ORDER BY notesId DESC")
    fun getAllNotes(): LiveData<List<Notes>>

    @Query("DELETE FROM notes_table WHERE notesId = :key")
    fun deleteNote(key: Long)
}
