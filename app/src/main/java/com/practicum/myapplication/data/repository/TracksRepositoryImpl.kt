package com.practicum.myapplication.data.repository

import com.practicum.myapplication.data.db.AppDatabase
import com.practicum.myapplication.data.db.entity.TrackEntity
import com.practicum.myapplication.data.dto.TracksSearchRequest
import com.practicum.myapplication.data.dto.TracksSearchResponse
import com.practicum.myapplication.domain.client.NetworkClient
import com.practicum.myapplication.domain.model.Track
import com.practicum.myapplication.domain.repository.TracksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Locale

class TracksRepositoryImpl(
    private val database: AppDatabase,
    private val networkClient: NetworkClient
) : TracksRepository {

    private val trackDao = database.trackDao()

    override suspend fun searchTracks(expression: String): List<Track> {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        if (response.resultCode == 200) {
            return (response as TracksSearchResponse).results.map {
                Track(
                    id = 0,
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

    override suspend fun getAllTracks(): List<Track> {
        return trackDao.getAllTracks().map { it.toDomain() }
    }

    override fun getTrackByNameAndArtist(track: Track): Flow<Track?> {
        return trackDao.getTrack(track.trackName, track.artistName).map { it?.toDomain() }
    }

    override suspend fun deleteTrackFromPlaylist(track: Track) {
        trackDao.removeTrack(
            name = track.trackName,
            artist = track.artistName,
            playlistId = track.playlistId
        )
    }

    override fun deleteTracksByPlaylistId(playlistId: Long) {
        kotlinx.coroutines.runBlocking {
            trackDao.deleteTracksByPlaylistId(playlistId)
        }
    }

    override suspend fun insertTrackToPlaylist(track: Track, playlistId: Long) {
        trackDao.insertTrack(TrackEntity.fromDomain(track, playlistId))
    }

    override suspend fun updateTrackFavoriteStatus(track: Track, isFavorite: Boolean) {
        trackDao.updateFavoriteStatus(track.id, isFavorite)
    }

    override fun getFavoriteTracks(): Flow<List<Track>> {
        return trackDao.getFavoriteTracks().map { entities ->
            entities.map { it.toDomain() }
        }
    }
}