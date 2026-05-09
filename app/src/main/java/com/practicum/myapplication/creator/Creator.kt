package com.practicum.myapplication.creator

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.practicum.myapplication.data.db.AppDatabase
import com.practicum.myapplication.data.network.RetrofitNetworkClient
import com.practicum.myapplication.data.repository.*
import com.practicum.myapplication.domain.repository.*
import ru.yandex.practicum.playlistmaker.data.preferences.SearchHistoryPreferences


private val Context.dataStore by preferencesDataStore(name = "settings")

object Creator {
    private var database: AppDatabase? = null

    private fun getDatabase(context: Context): AppDatabase {
        return database ?: synchronized(this) {
            database ?: AppDatabase.getInstance(context).also { database = it }
        }
    }

    fun getTracksRepository(context: Context): TracksRepository {
        return TracksRepositoryImpl(getDatabase(context), RetrofitNetworkClient())
    }

    fun getPlaylistsRepository(context: Context): PlaylistsRepository {
        return PlaylistsRepositoryImpl(getDatabase(context))
    }

    fun getSearchHistoryRepository(context: Context): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(SearchHistoryPreferences(context.dataStore))
    }
}