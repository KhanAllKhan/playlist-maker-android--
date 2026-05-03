package com.practicum.myapplication.data.repository

import com.practicum.myapplication.creator.DatabaseMock
import com.practicum.myapplication.domain.model.Playlist
import com.practicum.myapplication.domain.repository.PlaylistsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

class PlaylistsRepositoryImpl(
    private val scope: CoroutineScope,
    private val database: DatabaseMock
) : PlaylistsRepository {

    override fun getPlaylist(playlistId: Long): Flow<Playlist?> = database.getPlaylist(playlistId)

    override fun getAllPlaylists(): Flow<List<Playlist>> = database.getAllPlaylists()

    override suspend fun addNewPlaylist(name: String, description: String) {
        database.addNewPlaylist(name, description)
    }

    override suspend fun deletePlaylistById(id: Long) {
        database.deletePlaylistById(id)
    }
}