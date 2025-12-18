package com.practicum.myapplication.data.dto

import com.practicum.myapplication.domain.client.BaseResponse

class TracksSearchResponse(val results: List<TrackDto>) : BaseResponse()