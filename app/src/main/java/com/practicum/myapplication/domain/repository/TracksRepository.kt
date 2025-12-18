package com.practicum.myapplication.domain.repository

import com.practicum.myapplication.domain.model.Track

interface TracksRepository {
    suspend fun searchTracks(expression: String): List<Track>
    suspend fun getAllTracks(): List<Track>
}