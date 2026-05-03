package com.practicum.myapplication.creator

import com.practicum.myapplication.domain.model.Playlist
import com.practicum.myapplication.domain.model.Track
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DatabaseMock(
    private val scope: CoroutineScope,
) {
    private val tracks = mutableListOf(
        Track(1, "Владивосток 2000", "Мумий Троль", "2:38"),
        Track(2, "Группа крови", "Кино", "4:43"),
        Track(3, "Не смотри назад", "Ария", "5:12"),
        Track(4, "Звезда по имени Солнце", "Кино", "3:45"),
        Track(5, "London", "Аквариум", "4:32"),
        Track(6, "На заре", "Альянс", "3:50"),
        Track(7, "Перемен", "Кино", "4:56"),
        Track(8, "Розовый фламинго", "Сплин", "3:15"),
        Track(9, "Танцевать", "Мельница", "3:42"),
        Track(10, "Чёрный бумер", "Серега", "4:01")
    )

    private val playlists = mutableListOf<Playlist>()

    fun getAllTracks(): List<Track> = tracks.toList()

    fun getPlaylist(id: Long): Flow<Playlist?> = flow {
        emit(playlists.find { it.id == id })
    }

    fun getAllPlaylists(): Flow<List<Playlist>> = flow {
        delay(500)
        val filteredPlaylists = mutableListOf<Playlist>()
        playlists.forEach { playlist ->
            val playlistTracks = tracks.filter { it.playlistId == playlist.id }
            filteredPlaylists.add(playlist.copy(tracks = playlistTracks))
        }
        emit(filteredPlaylists.toList())
    }

    fun addNewPlaylist(name: String, description: String) {
        playlists.add(
            Playlist(
                id = playlists.size.toLong() + 1,
                name = name,
                description = description,
                tracks = emptyList()
            )
        )
    }

    fun getTrackByNameAndArtist(track: Track): Flow<Track?> = flow {
        emit(tracks.find { it.trackName == track.trackName && it.artistName == track.artistName })
    }

    fun insertTrack(track: Track) {
        val index = tracks.indexOfFirst { it.id == track.id }
        if (index != -1) {
            tracks[index] = track
        } else {
            tracks.add(track)
        }
    }

    fun getFavoriteTracks(): Flow<List<Track>> = flow {
        delay(300)
        emit(tracks.filter { it.favorite })
    }

    fun searchTracks(expression: String): List<Track> {
        return tracks.filter { it.trackName.contains(expression, true) }
    }

    fun deletePlaylistById(playlistId: Long) {
        playlists.removeIf { it.id == playlistId }
    }

    fun deleteTracksByPlaylistId(playlistId: Long) {
        tracks.forEachIndexed { index, track ->
            if (track.playlistId == playlistId) {
                tracks[index] = track.copy(playlistId = 0)
            }
        }
    }
}