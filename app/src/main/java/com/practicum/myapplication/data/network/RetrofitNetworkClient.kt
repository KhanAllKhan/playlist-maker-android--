package com.practicum.myapplication.data.network

import com.practicum.myapplication.creator.Storage
import com.practicum.myapplication.data.dto.TracksSearchRequest
import com.practicum.myapplication.data.dto.TracksSearchResponse
import com.practicum.myapplication.domain.client.BaseResponse
import com.practicum.myapplication.domain.client.NetworkClient

class RetrofitNetworkClient(
    private val storage: Storage
) : NetworkClient {

    override fun doRequest(dto: Any): BaseResponse {
        val request = dto as TracksSearchRequest
        val searchList = storage.search(request.expression)
        return TracksSearchResponse(searchList).apply { resultCode = 200 }
    }
}