package com.mutawalli.challenge4.data.local

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import androidx.room.Entity
import kotlinx.parcelize.Parcelize

@Entity(tableName = "Notepad")
@Parcelize
data class NoteEntity (
    @PrimaryKey(autoGenerate = true) var id:Int?,
    @ColumnInfo(name="judul") var judul:String,
    @ColumnInfo(name="catatan") var note:String
    ):Parcelable