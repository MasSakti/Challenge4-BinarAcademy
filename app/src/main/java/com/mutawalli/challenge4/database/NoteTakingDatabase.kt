package com.mutawalli.challenge4.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mutawalli.challenge4.data.local.EntityDAO
import com.mutawalli.challenge4.data.local.NoteEntity

@Database(entities = [NoteEntity::class], version = 1)
abstract class NoteTakingDatabase : RoomDatabase() {
    abstract fun noteDao(): EntityDAO

    companion object {
        @Volatile
        private var INSTANCE: NoteTakingDatabase? = null

        fun getInstance(context: Context): NoteTakingDatabase? {
            if (INSTANCE == null) {
                synchronized(NoteTakingDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context, NoteTakingDatabase::class.java,
                        "catatan.db"
                    ).build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}