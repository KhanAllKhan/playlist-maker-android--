package ru.yandex.practicum.playlistmaker.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SearchHistoryPreferences(
    private val dataStore: DataStore<Preferences>,
    private val coroutineScope: CoroutineScope = CoroutineScope(
        CoroutineName("search-history-preferences") + SupervisorJob()
    )
) {
    private val preferencesKey = stringPreferencesKey("search_history_key")

    fun addEntry(word: String) {
        if (word.isEmpty()) return

        coroutineScope.launch {
            dataStore.edit { preferences ->
                val historyString = preferences[preferencesKey].orEmpty()
                val history = if (historyString.isNotEmpty()) {
                    historyString.split(SEPARATOR).toMutableList()
                } else {
                    mutableListOf()
                }

                history.remove(word) // удаляем дубликат
                history.add(0, word) // добавляем в начало (самый свежий)

                val subList = if (history.size > MAX_ENTRIES) {
                    history.subList(0, MAX_ENTRIES)
                } else {
                    history
                }

                preferences[preferencesKey] = subList.joinToString(SEPARATOR)
            }
        }
    }

    suspend fun getEntries(): List<String> {
        // Достаем строку из DataStore и превращаем её обратно в список
        val preferences = dataStore.data.first()
        val historyString = preferences[preferencesKey].orEmpty()

        return if (historyString.isNotEmpty()) {
            historyString.split(SEPARATOR)
        } else {
            emptyList()
        }
    }

    companion object {
        private const val MAX_ENTRIES = 10
        private const val SEPARATOR = ","
    }
}