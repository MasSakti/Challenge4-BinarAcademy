package com.mutawalli.challenge4.data.local

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.mutawalli.challenge4.data.local.NoteEntity

@Dao
interface EntityDAO {
    @Query("SELECT * FROM Notepad")
    fun getAllData():List<NoteEntity>

    @Insert(onConflict = REPLACE)
    fun insertData(data: NoteEntity):Long

    @Update
    fun updateData(data: NoteEntity):Int

    @Delete
    fun deleteData(data: NoteEntity):Int
}