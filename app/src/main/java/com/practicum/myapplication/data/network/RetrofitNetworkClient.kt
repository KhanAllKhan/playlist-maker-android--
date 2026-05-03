package com.practicum.myapplication.data.network

import com.practicum.myapplication.data.dto.TracksSearchRequest
import com.practicum.myapplication.data.dto.TracksSearchResponse
import com.practicum.myapplication.domain.client.BaseResponse
import com.practicum.myapplication.domain.client.NetworkClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient : NetworkClient {

    private val iTunesBaseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService = retrofit.create(ITunesApi::class.java)

    override fun doRequest(dto: Any): BaseResponse {
        if (dto is TracksSearchRequest) {
            val response = iTunesService.search(dto.expression).execute()
            val body = response.body() ?: BaseResponse()
            return body.apply { resultCode = response.code() }
        } else {
            return BaseResponse().apply { resultCode = 400 }
        }
    }
}