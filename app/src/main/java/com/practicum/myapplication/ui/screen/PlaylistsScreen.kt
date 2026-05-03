package com.practicum.myapplication.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.practicum.myapplication.domain.model.Track
import com.practicum.myapplication.ui.components.TrackListItem
import com.practicum.myapplication.ui.viewmodel.PlaylistViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistScreen(
    modifier: Modifier = Modifier,
    viewModel: PlaylistViewModel,
    navigateBack: () -> Unit,
    onTrackClick: (Track) -> Unit
) {
    val playlistState by viewModel.playlist.collectAsState(initial = null)

    // Состояния для показа диалогов
    var showDeletePlaylistDialog by remember { mutableStateOf(false) }
    var trackToDelete by remember { mutableStateOf<Track?>(null) }

    // --- ДИАЛОГ УДАЛЕНИЯ ПЛЕЙЛИСТА ---
    if (showDeletePlaylistDialog) {
        AlertDialog(
            onDismissRequest = { showDeletePlaylistDialog = false },
            title = { Text("Хотите удалить плейлист «${playlistState?.name}»?") },
            confirmButton = {
                TextButton(onClick = {
                    showDeletePlaylistDialog = false
                    viewModel.deletePlaylist { navigateBack() } // Удаляем и выходим
                }) {
                    Text("ДА")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeletePlaylistDialog = false }) {
                    Text("НЕТ")
                }
            }
        )
    }

    // --- ДИАЛОГ УДАЛЕНИЯ ТРЕКА ---
    trackToDelete?.let { track ->
        AlertDialog(
            onDismissRequest = { trackToDelete = null },
            title = { Text("Хотите удалить трек?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteTrack(track)
                    trackToDelete = null // Закрываем диалог
                }) {
                    Text("ДА")
                }
            },
            dismissButton = {
                TextButton(onClick = { trackToDelete = null }) {
                    Text("НЕТ")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = playlistState?.name ?: "Плейлист") },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
                // ДОБАВЛЕНО: Иконка удаления плейлиста в шапке
                actions = {
                    IconButton(onClick = { showDeletePlaylistDialog = true }) {
                        Icon(Icons.Default.Delete, contentDescription = "Удалить плейлист")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).background(Color.White)
        ) {
            playlistState?.let { playlist ->
                if (playlist.description.isNotEmpty()) {
                    Text(
                        text = playlist.description,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }

                if (playlist.tracks.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "В этом плейлисте пока нет треков", color = Color.Gray)
                    }
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(playlist.tracks) { track ->
                            TrackListItem(
                                track = track,
                                onClick = { onTrackClick(track) },
                                // ДОБАВЛЕНО: Вызов диалога при долгом нажатии
                                onLongClick = { trackToDelete = track }
                            )
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                thickness = 0.5.dp,
                                color = Color.LightGray
                            )
                        }
                    }
                }
            } ?: run {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}