package com.practicum.myapplication.navigation

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.practicum.myapplication.ui.screen.*
import com.practicum.myapplication.ui.viewmodel.PlaylistsViewModel
import com.practicum.myapplication.domain.model.Track

@Composable
fun PlaylistHost(
    navController: NavHostController,
    playlistsViewModel: PlaylistsViewModel
) {
    var selectedTrack by remember { mutableStateOf<Track?>(null) }

    NavHost(
        navController = navController,
        startDestination = Screen.MAIN.name
    ) {
        composable(Screen.MAIN.name) {
            MainScreen(
                onSearchClick = { navController.navigate(Screen.SEARCH.name) },
                onSettingsClick = { navController.navigate(Screen.SETTINGS.name) },
                onPlaylistsClick = { navController.navigate(Screen.ALL_TRACKS.name) },
                onFavoritesClick = { navController.navigate(Screen.FAVORITES.name) }
            )
        }

        composable(Screen.SEARCH.name) {
            SearchScreen(
                onBackClick = { navController.popBackStack() },
                onTrackClick = { track ->
                    selectedTrack = track
                    navController.navigate(Screen.TRACK_DETAILS.name)
                }
            )
        }

        composable(Screen.ALL_TRACKS.name) {
            PlaylistsScreen(
                viewModel = playlistsViewModel,
                onBackClick = { navController.popBackStack() },
                onNewPlaylistClick = { navController.navigate(Screen.NEW_PLAYLIST.name) }
            )
        }

        composable(Screen.NEW_PLAYLIST.name) {
            NewPlaylistScreen(
                viewModel = playlistsViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.TRACK_DETAILS.name) {
            selectedTrack?.let { track ->
                TrackDetailsScreen(
                    track = track,
                    viewModel = playlistsViewModel,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }

        composable(Screen.FAVORITES.name) {
            FavoritesScreen(
                viewModel = playlistsViewModel,
                onBackClick = { navController.popBackStack() },
                onTrackClick = { track ->
                    selectedTrack = track
                    navController.navigate(Screen.TRACK_DETAILS.name)
                }
            )
        }

        composable(Screen.SETTINGS.name) {
            SettingsScreen(onBackClick = { navController.popBackStack() })
        }
    }
}