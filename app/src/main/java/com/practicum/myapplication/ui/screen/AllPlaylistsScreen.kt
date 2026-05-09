package com.practicum.myapplication.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.practicum.myapplication.ui.components.PlaylistListItem
import com.practicum.myapplication.ui.viewmodel.AllPlaylistsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllPlaylistsScreen(
    viewModel: AllPlaylistsViewModel,
    onBackClick: () -> Unit,
    onNewPlaylistClick: () -> Unit,
    navigateToPlaylist: (Long) -> Unit
) {
    val playlists by viewModel.playlists.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Плейлисты") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNewPlaylistClick,
                containerColor = Color.Gray,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(Icons.Filled.Add, contentDescription = null)
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.White)
        ) {
            if (playlists.isEmpty()) {
                Text(
                    text = "Нет созданных плейлистов",
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.Gray
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(playlists.size) { index ->
                        val playlist = playlists[index]
                        PlaylistListItem(
                            playlist = playlist,
                            onClick = { navigateToPlaylist(playlist.id) }
                        )
                        HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)
                    }
                }
            }
        }
    }
}