package com.practicum.myapplication.domain.model

data class Track(
    val id: Long = 0,
    val trackName: String,
    val artistName: String,
    val trackTime: String,
    val favorite: Boolean = false,
    val playlistId: Long = 0
)