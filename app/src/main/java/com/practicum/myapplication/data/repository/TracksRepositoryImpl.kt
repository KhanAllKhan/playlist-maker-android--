package com.practicum.myapplication.data.repository

import com.practicum.myapplication.data.dto.TracksSearchRequest
import com.practicum.myapplication.data.dto.TracksSearchResponse
import com.practicum.myapplication.domain.client.NetworkClient
import com.practicum.myapplication.domain.model.Track
import com.practicum.myapplication.domain.repository.TracksRepository
import kotlinx.coroutines.delay

class TracksRepositoryImpl(
    private val networkClient: NetworkClient
) : TracksRepository {

    override suspend fun searchTracks(expression: String): List<Track> {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        delay(1000)

        return if (response.resultCode == 200) {
            (response as TracksSearchResponse).results.map { dtoToTrack(it) }
        } else {
            emptyList()
        }
    }

    override suspend fun getAllTracks(): List<Track> {
        delay(1000)

        return listOf(
            Track("Владивосток 2000", "Мумий Троль", "2:38"),
            Track("Группа крови", "Кино", "4:43"),
            Track("Не смотри назад", "Ария", "5:12"),
            Track("Звезда по имени Солнце", "Кино", "3:45"),
            Track("Лондон", "Аквариум", "4:32"),
            Track("На заре", "Альянс", "3:50"),
            Track("Перемен", "Кино", "4:56"),
            Track("Розовый фламинго", "Сплин", "3:15"),
            Track("Танцевать", "Мельница", "3:42"),
            Track("Чёрный бумер", "Серега", "4:01")
        )
    }

    private fun dtoToTrack(dto: com.practicum.myapplication.data.dto.TrackDto): Track {
        val seconds = dto.trackTimeMillis / 1000
        val minutes = seconds / 60
        val trackTime = "%02d:%02d".format(minutes, seconds % 60)
        return Track(dto.trackName, dto.artistName, trackTime)
    }
}