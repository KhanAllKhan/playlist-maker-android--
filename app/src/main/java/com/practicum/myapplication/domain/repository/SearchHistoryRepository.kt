package com.practicum.myapplication.domain.repository

interface SearchHistoryRepository {
    fun addTrackToHistory(word: String)

    suspend fun getHistory(): List<String>
}