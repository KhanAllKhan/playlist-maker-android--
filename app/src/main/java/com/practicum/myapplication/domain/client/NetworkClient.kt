package com.practicum.myapplication.domain.client

interface NetworkClient {
    fun doRequest(dto: Any): BaseResponse
}

open class BaseResponse {
    var resultCode = 0
}