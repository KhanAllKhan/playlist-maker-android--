package com.practicum.myapplication.domain.model

data class Track(
    val id: Long = 0, // Уникальный ID записи
    val trackName: String,
    val artistName: String,
    val trackTime: String,
    val artworkUrl100: String? = null,
    val favorite: Boolean = false,
    val playlistId: Long = -1 // К какому плейлисту относится
)