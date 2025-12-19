package com.practicum.myapplication.ui.viewmodel



data class SearchScreenState(
    val query: String = "",
    val tracks: List<TrackUi> = emptyList(),
    val isLoading: Boolean = false,
    val isSearchPerformed: Boolean = false
)

data class TrackUi(
    val trackName: String,
    val artistName: String,
    val trackTime: String
)

