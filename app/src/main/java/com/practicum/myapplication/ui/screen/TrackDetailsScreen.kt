package com.practicum.myapplication.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practicum.myapplication.domain.model.Track
import com.practicum.myapplication.ui.viewmodel.AllPlaylistsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackDetailsScreen(
    track: Track,
    viewModel: AllPlaylistsViewModel,
    onBackClick: () -> Unit
) {
    var isFavorite by remember { mutableStateOf(track.favorite) }
    var showBottomSheet by remember { mutableStateOf(false) }
    val playlists by viewModel.playlists.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Детали трека") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.size(240.dp).background(Color.LightGray))
            Spacer(modifier = Modifier.height(24.dp))
            Text(track.trackName, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Text(track.artistName, fontSize = 16.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(32.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                IconButton(onClick = {
                    isFavorite = !isFavorite
                    viewModel.toggleFavorite(track, isFavorite)
                }) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = null,
                        tint = if (isFavorite) Color.Red else Color.Black
                    )
                }
                Button(onClick = { showBottomSheet = true }) {
                    Text("В плейлист")
                }
            }
        }

        if (showBottomSheet) {
            ModalBottomSheet(onDismissRequest = { showBottomSheet = false }) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Выберите плейлист",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    if (playlists.isEmpty()) {
                        Text(
                            "Сначала создайте плейлист",
                            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                            textAlign = TextAlign.Center,
                            color = Color.Gray
                        )
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxWidth()) {
                            items(playlists) { playlist ->
                                TextButton(
                                    onClick = {
                                        viewModel.insertTrackToPlaylist(track, playlist.id)
                                        showBottomSheet = false
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(playlist.name, fontSize = 18.sp)
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}