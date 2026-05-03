package com.practicum.myapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.myapplication.creator.DatabaseMock
import com.practicum.myapplication.data.network.RetrofitNetworkClient
import com.practicum.myapplication.data.repository.PlaylistsRepositoryImpl
import com.practicum.myapplication.data.repository.TracksRepositoryImpl
import com.practicum.myapplication.domain.model.Playlist
import com.practicum.myapplication.domain.model.Track
import com.practicum.myapplication.domain.repository.PlaylistsRepository
import com.practicum.myapplication.domain.repository.TracksRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class AllPlaylistsViewModel : ViewModel() {
    private val databaseMock = DatabaseMock(viewModelScope)

    // Создаем клиент для сетевых запросов
    private val networkClient = RetrofitNetworkClient()

    private val playlistsRepository: PlaylistsRepository = PlaylistsRepositoryImpl(viewModelScope, databaseMock)

    private val tracksRepository: TracksRepository = TracksRepositoryImpl(
        viewModelScope,
        databaseMock,
        networkClient
    )

    val playlists: Flow<List<Playlist>> = playlistsRepository.getAllPlaylists()
    val favoriteList: Flow<List<Track>> = tracksRepository.getFavoriteTracks()

    fun createNewPlayList(namePlaylist: String, description: String) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistsRepository.addNewPlaylist(namePlaylist, description)
        }
    }

    fun insertTrackToPlaylist(track: Track, playlistId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            tracksRepository.insertTrackToPlaylist(track, playlistId)
        }
    }

    fun toggleFavorite(track: Track, isFavorite: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            tracksRepository.updateTrackFavoriteStatus(track, isFavorite)
        }
    }
}