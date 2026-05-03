package com.practicum.myapplication.data.dto

import com.practicum.myapplication.domain.client.BaseResponse

data class TracksSearchResponse(
    val resultCount: Int,
    val results: List<TrackDto>
) : BaseResponse()