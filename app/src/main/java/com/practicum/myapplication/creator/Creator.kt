package com.practicum.myapplication.creator

import com.practicum.myapplication.data.network.RetrofitNetworkClient
import com.practicum.myapplication.data.repository.PlaylistsRepositoryImpl
import com.practicum.myapplication.data.repository.TracksRepositoryImpl
import com.practicum.myapplication.domain.repository.PlaylistsRepository
import com.practicum.myapplication.domain.repository.TracksRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope

object Creator {

    private val databaseMock by lazy { DatabaseMock(GlobalScope) }

    private val networkClient by lazy { RetrofitNetworkClient() }

    fun getTracksRepository(scope: CoroutineScope = GlobalScope): TracksRepository {
        return TracksRepositoryImpl(
            scope = scope,
            database = databaseMock,
            networkClient = networkClient
        )
    }

    fun getPlaylistsRepository(scope: CoroutineScope = GlobalScope): PlaylistsRepository {
        return PlaylistsRepositoryImpl(
            scope = scope,
            database = databaseMock
        )
    }
}