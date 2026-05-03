package com.practicum.myapplication.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.practicum.myapplication.creator.Creator
import com.practicum.myapplication.domain.model.SearchState
import com.practicum.myapplication.domain.repository.TracksRepository
import com.practicum.myapplication.domain.repository.SearchHistoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
    private val tracksRepository: TracksRepository,
    private val searchHistoryRepository: SearchHistoryRepository // Добавили репозиторий истории
) : ViewModel() {

    private val _searchScreenState = MutableStateFlow<SearchState>(SearchState.Initial)
    val searchScreenState: StateFlow<SearchState> = _searchScreenState.asStateFlow()

    // Состояние истории поиска
    private val _historyState = MutableStateFlow<List<String>>(emptyList())
    val historyState: StateFlow<List<String>> = _historyState.asStateFlow()

    init {
        loadHistory()
    }

    fun loadHistory() {
        viewModelScope.launch {
            _historyState.value = searchHistoryRepository.getHistory()
        }
    }

    fun search(whatSearch: String) {
        // При поиске сохраняем запрос в историю
        searchHistoryRepository.addTrackToHistory(whatSearch)

        viewModelScope.launch(Dispatchers.IO) {
            _searchScreenState.update { SearchState.Loading }
            try {
                val list = tracksRepository.searchTracks(expression = whatSearch)
                _searchScreenState.update { SearchState.Success(foundList = list) }
            } catch (e: Exception) {
                _searchScreenState.update { SearchState.Error(e.message ?: "Error") }
            }
        }
    }

    fun clearSearch() {
        _searchScreenState.update { SearchState.Initial }
        loadHistory() // Обновляем список истории при очистке
    }

    companion object {
        fun getViewModelFactory(context: Context): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SearchViewModel(
                        Creator.getTracksRepository(context),
                        Creator.getSearchHistoryRepository(context)
                    ) as T
                }
            }
    }
}