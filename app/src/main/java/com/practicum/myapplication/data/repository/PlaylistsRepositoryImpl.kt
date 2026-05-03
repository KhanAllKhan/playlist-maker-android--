package com.practicum.myapplication.data.repository

import com.practicum.myapplication.data.db.AppDatabase
import com.practicum.myapplication.data.db.entity.PlaylistEntity
import com.practicum.myapplication.domain.model.Playlist
import com.practicum.myapplication.domain.model.Track
import com.practicum.myapplication.domain.repository.PlaylistsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class PlaylistsRepositoryImpl(
    private val database: AppDatabase
) : PlaylistsRepository {

    private val playlistDao = database.playlistDao()
    private val trackDao = database.trackDao()

    override suspend fun addNewPlaylist(name: String, description: String) {
        playlistDao.insertPlaylist(
            PlaylistEntity(
                name = name,
                description = description,
                imagePath = null
            )
        )
    }

    override fun getAllPlaylists(): Flow<List<Playlist>> {
        // Комбинируем потоки плейлистов и треков
        return combine(
            playlistDao.getAllPlaylists(),
            trackDao.getAllTracksFlow()
        ) { playlists, tracks ->
            val domainTracks = tracks.map { it.toDomain() }
            playlists.map { playlistEntity ->
                // Фильтруем треки, которые принадлежат только этому плейлисту
                val playlistTracks = domainTracks.filter { it.playlistId == playlistEntity.id }
                playlistEntity.toDomain(playlistTracks)
            }
        }
    }

    override fun getPlaylist(playlistId: Long): Flow<Playlist?> {
        return combine(
            playlistDao.getPlaylistById(playlistId),
            trackDao.getAllTracksFlow()
        ) { playlist, tracks ->
            playlist?.let { entity ->
                val domainTracks = tracks.map { it.toDomain() }
                // Фильтруем треки именно для этого открытого плейлиста
                val playlistTracks = domainTracks.filter { it.playlistId == entity.id }
                entity.toDomain(playlistTracks)
            }
        }
    }

    override suspend fun deletePlaylistById(id: Long) {
        playlistDao.deletePlaylistById(id)
    }

    private fun PlaylistEntity.toDomain(tracks: List<Track>): Playlist {
        return Playlist(
            id = this.id,
            name = this.name,
            description = this.description,
            tracks = tracks // Теперь здесь реальные треки, а не пустой список
        )
    }
}