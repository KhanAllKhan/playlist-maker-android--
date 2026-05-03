package com.practicum.myapplication.creator

import com.practicum.myapplication.data.dto.TrackDto

class Storage {
    private val listTracks = listOf(
        TrackDto(
            trackName = "Владивосток 2000",
            artistName = "Мумий Троль",
            trackTimeMillis = 158000,
            artworkUrl100 = null // Добавьте это поле
        ),
        TrackDto(
            trackName = "Группа крови",
            artistName = "Кино",
            trackTimeMillis = 283000,
            artworkUrl100 = null
        ),
        TrackDto(
            trackName = "Не смотри назад",
            artistName = "Ария",
            trackTimeMillis = 312000,
            artworkUrl100 = null
        ),
        TrackDto(
            trackName = "Звезда по имени Солнце",
            artistName = "Кино",
            trackTimeMillis = 225000,
            artworkUrl100 = null
        ),
        TrackDto(
            trackName = "Лондон",
            artistName = "Аквариум",
            trackTimeMillis = 272000,
            artworkUrl100 = null
        ),
        TrackDto(
            trackName = "На заре",
            artistName = "Альянс",
            trackTimeMillis = 230000,
            artworkUrl100 = null
        ),
        TrackDto(
            trackName = "Peremen",
            artistName = "Кино",
            trackTimeMillis = 296000,
            artworkUrl100 = null
        ),
        TrackDto(
            trackName = "Розовый фламинго",
            artistName = "Сплин",
            trackTimeMillis = 195000,
            artworkUrl100 = null
        ),
        TrackDto(
            trackName = "Танцевать",
            artistName = "Мельница",
            trackTimeMillis = 222000,
            artworkUrl100 = null
        ),
        TrackDto(
            trackName = "Чёрный бумер",
            artistName = "Серега",
            trackTimeMillis = 241000,
            artworkUrl100 = null
        )
    )

    fun search(request: String): List<TrackDto> {
        return listTracks.filter {
            it.trackName.lowercase().contains(request.lowercase())
        }
    }

    fun getAllTracks(): List<TrackDto> {
        return listTracks
    }
}