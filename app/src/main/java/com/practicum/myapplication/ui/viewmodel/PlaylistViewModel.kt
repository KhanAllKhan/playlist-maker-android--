package com.practicum.myapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.myapplication.domain.model.Playlist
import com.practicum.myapplication.domain.model.Track
import com.practicum.myapplication.domain.repository.PlaylistsRepository
import com.practicum.myapplication.domain.repository.TracksRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlaylistViewModel(
    private val playlistsRepository: PlaylistsRepository,
    private val tracksRepository: TracksRepository, // ДОБАВЛЕНО
    private val playlistId: Long
) : ViewModel() {

    private val _playlistState = MutableStateFlow<Playlist?>(null)
    val playlist: StateFlow<Playlist?> = _playlistState.asStateFlow()

    init {
        loadPlaylist()
    }

    private fun loadPlaylist() {
        viewModelScope.launch {
            playlistsRepository.getPlaylist(playlistId).collect { result ->
                _playlistState.value = result
            }
        }
    }

    // ДОБАВЛЕНО: Удаление трека из плейлиста
    fun deleteTrack(track: Track) {
        viewModelScope.launch(Dispatchers.IO) {
            tracksRepository.deleteTrackFromPlaylist(track)
        }
    }

    // ДОБАВЛЕНО: Удаление самого плейлиста и выход
    fun deletePlaylist(onComplete: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistsRepository.deletePlaylistById(playlistId)
            tracksRepository.deleteTracksByPlaylistId(playlistId) // Очищаем треки этого плейлиста

            withContext(Dispatchers.Main) {
                onComplete() // Вызываем возврат назад (popBackStack)
            }
        }
    }
}