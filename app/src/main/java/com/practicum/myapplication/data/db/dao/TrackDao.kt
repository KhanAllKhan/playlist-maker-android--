package com.practicum.myapplication.data.db.dao

import androidx.room.*
import com.practicum.myapplication.data.db.entity.TrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackEntity)

    @Query("UPDATE track_table SET isFavorite = :isFavorite WHERE trackId = :trackId")
    suspend fun updateFavoriteStatus(trackId: Long, isFavorite: Boolean)

    @Query("SELECT * FROM track_table WHERE isFavorite = 1")
    fun getFavoriteTracks(): Flow<List<TrackEntity>>

    @Query("SELECT * FROM track_table")
    suspend fun getAllTracks(): List<TrackEntity>

    // ДОБАВЛЕНО: Поток всех треков для привязки к плейлистам
    @Query("SELECT * FROM track_table")
    fun getAllTracksFlow(): Flow<List<TrackEntity>>

    @Query("SELECT * FROM track_table WHERE trackName = :name AND artistName = :artist LIMIT 1")
    fun getTrack(name: String, artist: String): Flow<TrackEntity?>

    @Delete
    suspend fun deleteTrack(track: TrackEntity)

    @Query("DELETE FROM track_table WHERE playlistId = :playlistId")
    suspend fun deleteTracksByPlaylistId(playlistId: Long)
}