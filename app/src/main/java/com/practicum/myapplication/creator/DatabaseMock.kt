package com.practicum.myapplication.creator

import com.practicum.myapplication.domain.model.Playlist
import com.practicum.myapplication.domain.model.Track
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class DatabaseMock(private val scope: CoroutineScope) {
    // Простой счётчик для ID (начинаем с 2, так как 1 занят хардкодом)
    private var nextId = 2L

    private val initialTracks = listOf(
        Track(trackName = "Yesterday", artistName = "The Beatles", trackTime = "02:05", playlistId = 1L),
        Track(trackName = "Stayin' Alive", artistName = "Bee Gees", trackTime = "04:45", playlistId = 1L)
    )
    private val initialPlaylists = listOf(
        Playlist(id = 1L, name = "Тестовый плейлист", description = "Если ты это видишь, база работает")
    )

    private val allTracks = MutableStateFlow<List<Track>>(initialTracks)
    private val allPlaylists = MutableStateFlow<List<Playlist>>(initialPlaylists)

    fun insertTrack(track: Track) {
        val currentList = allTracks.value.toMutableList()
        val index = currentList.indexOfFirst {
            it.trackName == track.trackName && it.artistName == track.artistName
        }
        if (index != -1) {
            currentList[index] = track
        } else {
            currentList.add(track)
        }
        allTracks.value = currentList
    }

    fun getAllPlaylists(): Flow<List<Playlist>> {
        return combine(allPlaylists, allTracks) { playlists, tracks ->
            playlists.map { playlist ->
                playlist.copy(tracks = tracks.filter { it.playlistId == playlist.id })
            }
        }
    }

    fun getPlaylist(playlistId: Long): Flow<Playlist?> {
        return combine(allPlaylists, allTracks) { playlists, tracks ->
            playlists.find { it.id == playlistId }?.copy(
                tracks = tracks.filter { it.playlistId == playlistId }
            )
        }
    }

    fun addNewPlaylist(name: String, description: String) {
        val newList = allPlaylists.value.toMutableList()
        val idForNew = nextId++ // Используем простой Long ID
        newList.add(Playlist(id = idForNew, name = name, description = description))
        allPlaylists.value = newList
    }

    fun getAllTracks(): List<Track> = allTracks.value

    // ИСПРАВЛЕНО: Теперь этот метод существует и репозиторий его увидит
    fun getTrackByNameAndArtist(track: Track): Flow<Track?> = allTracks.map { list ->
        list.find { it.trackName == track.trackName && it.artistName == track.artistName }
    }

    fun deleteTracksByPlaylistId(playlistId: Long) {
        allTracks.value = allTracks.value.map {
            if (it.playlistId == playlistId) it.copy(playlistId = -1L) else it
        }
    }

    fun getFavoriteTracks(): Flow<List<Track>> = allTracks.map { list -> list.filter { it.favorite } }

    fun deletePlaylistById(id: Long) {
        val newList = allPlaylists.value.toMutableList()
        newList.removeAll { it.id == id }
        allPlaylists.value = newList
    }
}