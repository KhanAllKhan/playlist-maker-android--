package com.practicum.myapplication.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.practicum.myapplication.domain.model.Track

@Entity(tableName = "track_table")
data class TrackEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val trackId: Long, // Оригинальный ID из API (если есть)
    val trackName: String,
    val artistName: String,
    val trackTime: String,
    val artworkUrl100: String?,
    val isFavorite: Boolean,
    val playlistId: Long // Внешний ключ для связи с плейлистом
) {

    fun toDomain(): Track {
        return Track(
            id = this.trackId,
            trackName = this.trackName,
            artistName = this.artistName,
            trackTime = this.trackTime,
            artworkUrl100 = this.artworkUrl100,
            favorite = this.isFavorite,
            playlistId = this.playlistId
        )
    }

    companion object {

        fun fromDomain(track: Track, playlistId: Long): TrackEntity {
            return TrackEntity(
                trackId = track.id,
                trackName = track.trackName,
                artistName = track.artistName,
                trackTime = track.trackTime,
                artworkUrl100 = track.artworkUrl100,
                isFavorite = track.favorite,
                playlistId = playlistId
            )
        }
    }
}