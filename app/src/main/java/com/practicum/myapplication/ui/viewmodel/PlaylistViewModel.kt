package com.practicum.myapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.myapplication.domain.model.Playlist
import com.practicum.myapplication.domain.repository.PlaylistsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val playlistsRepository: PlaylistsRepository,
    private val playlistId: Long
) : ViewModel() {

    private val _playlistState = MutableStateFlow<Playlist?>(null)
    val playlist: StateFlow<Playlist?> = _playlistState.asStateFlow()

    init {
        loadPlaylist()
    }

    private fun loadPlaylist() {
        viewModelScope.launch {
            // Подписываемся на изменения плейлиста в репозитории
            playlistsRepository.getPlaylist(playlistId).collect { result ->
                _playlistState.value = result
            }
        }
    }
}