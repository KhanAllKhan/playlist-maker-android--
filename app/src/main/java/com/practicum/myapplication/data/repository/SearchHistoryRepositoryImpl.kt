package com.practicum.myapplication.data.repository

import com.practicum.myapplication.domain.repository.SearchHistoryRepository
import ru.yandex.practicum.playlistmaker.data.preferences.SearchHistoryPreferences

class SearchHistoryRepositoryImpl(
    private val searchHistoryPreferences: SearchHistoryPreferences
) : SearchHistoryRepository {

    override fun addTrackToHistory(word: String) {
        searchHistoryPreferences.addEntry(word)
    }

    override suspend fun getHistory(): List<String> {
        return searchHistoryPreferences.getEntries()
    }
}