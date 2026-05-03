package com.practicum.myapplication.data.repository

import com.practicum.myapplication.creator.DatabaseMock
import com.practicum.myapplication.data.dto.TracksSearchRequest
import com.practicum.myapplication.data.dto.TracksSearchResponse
import com.practicum.myapplication.domain.client.NetworkClient
import com.practicum.myapplication.domain.model.Track
import com.practicum.myapplication.domain.repository.TracksRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Locale

class TracksRepositoryImpl(
    private val scope: CoroutineScope,
    private val database: DatabaseMock,
    private val networkClient: NetworkClient
) : TracksRepository {

    override suspend fun searchTracks(expression: String): List<Track> {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        if (response.resultCode == 200) {
            return (response as TracksSearchResponse).results.map {
                Track(
                    trackName = it.trackName,
                    artistName = it.artistName,
                    trackTime = SimpleDateFormat("mm:ss", Locale.getDefault()).format(it.trackTimeMillis),
                    artworkUrl100 = it.artworkUrl100
                )
            }
        } else {
            throw Exception("Ошибка сервера")
        }
    }

    override suspend fun getAllTracks(): List<Track> = database.getAllTracks()

    override fun getTrackByNameAndArtist(track: Track): Flow<Track?> =
        database.getTrackByNameAndArtist(track)

    override suspend fun insertTrackToPlaylist(track: Track, playlistId: Long) =
        database.insertTrack(track.copy(playlistId = playlistId))

    override suspend fun deleteTrackFromPlaylist(track: Track) =
        database.insertTrack(track.copy(playlistId = -1L)) // Использовано -1L для чистоты

    override suspend fun updateTrackFavoriteStatus(track: Track, isFavorite: Boolean) =
        database.insertTrack(track.copy(favorite = isFavorite))

    override fun deleteTracksByPlaylistId(playlistId: Long) =
        database.deleteTracksByPlaylistId(playlistId)

    override fun getFavoriteTracks(): Flow<List<Track>> = database.getFavoriteTracks()
}