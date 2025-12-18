package com.practicum.myapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.myapplication.domain.model.SearchState
import com.practicum.myapplication.domain.repository.TracksRepository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TracksViewModel(
    private val repository: TracksRepository
) : ViewModel() {


    private val _allTracksScreenState = MutableStateFlow<SearchState>(SearchState.Loading)
    val allTracksScreenState: StateFlow<SearchState> = _allTracksScreenState.asStateFlow()

    fun fetchAllTracks() {
        viewModelScope.launch {
            try {
                _allTracksScreenState.update { SearchState.Loading }
                val list = repository.getAllTracks()
                _allTracksScreenState.update { SearchState.Success(foundList = list) }
            } catch (e: Exception) {
                _allTracksScreenState.update { SearchState.Error(e.message ?: "Ошибка загрузки") }
            }
        }
    }
}