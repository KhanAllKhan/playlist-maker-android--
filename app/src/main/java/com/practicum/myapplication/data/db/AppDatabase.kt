package com.practicum.myapplication.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.practicum.myapplication.data.db.dao.PlaylistDao
import com.practicum.myapplication.data.db.dao.TrackDao
import com.practicum.myapplication.data.db.entity.PlaylistEntity
import com.practicum.myapplication.data.db.entity.TrackEntity

@Database(entities = [PlaylistEntity::class, TrackEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun playlistDao(): PlaylistDao
    abstract fun trackDao(): TrackDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "playlist_maker_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}