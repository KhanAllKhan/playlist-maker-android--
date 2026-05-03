package com.practicum.myapplication.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.practicum.myapplication.R
import com.practicum.myapplication.creator.Creator
import com.practicum.myapplication.domain.model.SearchState
import com.practicum.myapplication.domain.model.Track
import com.practicum.myapplication.ui.components.TrackListItem
import com.practicum.myapplication.ui.viewmodel.TracksViewModel

@Composable
fun AllTracksScreen(
    onBackClick: () -> Unit = {},
    onNewPlaylistClick: () -> Unit = {},
    onTrackClick: (Track) -> Unit = {},
    viewModel: TracksViewModel = viewModel(
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return TracksViewModel(Creator.getTracksRepository()) as T
            }
        }
    )
) {
    val screenState by viewModel.allTracksScreenState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchAllTracks()
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNewPlaylistClick,
                containerColor = Color.Gray,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF4B74E6))
                    .padding(16.dp)
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = null,
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Плейлисты",
                    color = Color.White,
                    fontSize = 20.sp
                )
            }

            when (screenState) {
                is SearchState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is SearchState.Success -> {
                    val tracks = (screenState as SearchState.Success).foundList
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                    ) {
                        items(tracks) { track ->
                            Column {
                                TrackListItem(
                                    track = track,
                                    modifier = Modifier
                                        .clickable { onTrackClick(track) }
                                        .padding(vertical = 12.dp)
                                )
                                Divider(
                                    thickness = 0.5.dp,
                                    color = Color.LightGray
                                )
                            }
                        }
                    }
                }

                is SearchState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = (screenState as SearchState.Error).error, color = Color.Red)
                    }
                }

                else -> {}
            }
        }
    }
}