package com.practicum.myapplication.domain.model

data class Playlist(
    val id: Long,
    val name: String,
    val description: String,
    val tracks: List<Track> = emptyList()
)