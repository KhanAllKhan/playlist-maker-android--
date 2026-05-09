package com.practicum.myapplication.navigation

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.practicum.myapplication.creator.Creator
import com.practicum.myapplication.ui.screen.*
import com.practicum.myapplication.ui.viewmodel.AllPlaylistsViewModel
import com.practicum.myapplication.ui.viewmodel.PlaylistViewModel
import com.practicum.myapplication.domain.model.Track
import androidx.compose.ui.platform.LocalContext

@Composable
fun PlaylistHost(
    navController: NavHostController,
    allPlaylistsViewModel: AllPlaylistsViewModel
) {
    val context = LocalContext.current.applicationContext
    var selectedTrack by remember { mutableStateOf<Track?>(null) }

    NavHost(navController = navController, startDestination = Screen.MAIN.name) {
        composable(Screen.MAIN.name) {
            MainScreen(
                onSearchClick = { navController.navigate(Screen.SEARCH.name) },
                onSettingsClick = { navController.navigate(Screen.SETTINGS.name) },
                onPlaylistsClick = { navController.navigate(Screen.ALL_TRACKS.name) },
                onFavoritesClick = { navController.navigate(Screen.FAVORITES.name) }
            )
        }

        composable(Screen.ALL_TRACKS.name) {
            // ВАЖНО: Здесь должен быть AllPlaylistsScreen, а не AllTracksScreen!
            AllPlaylistsScreen(
                viewModel = allPlaylistsViewModel,
                onBackClick = { navController.popBackStack() },
                onNewPlaylistClick = { navController.navigate(Screen.NEW_PLAYLIST.name) },
                navigateToPlaylist = { id ->
                    // Переход в детали конкретного плейлиста
                    navController.navigate("${Screen.PLAYLIST_DETAILS.name}/$id")
                }
            )
        }

        composable(
            route = "${Screen.PLAYLIST_DETAILS.name}/{playlistId}",
            arguments = listOf(navArgument("playlistId") { type = NavType.LongType })
        ) { backStackEntry ->
            val playlistId = backStackEntry.arguments?.getLong("playlistId") ?: -1L
            val playlistViewModel: PlaylistViewModel = viewModel(
                key = playlistId.toString(),
                factory = object : androidx.lifecycle.ViewModelProvider.Factory {
                    @Suppress("UNCHECKED_CAST")
                    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                        return PlaylistViewModel(
                            playlistsRepository = Creator.getPlaylistsRepository(context),
                            tracksRepository = Creator.getTracksRepository(context), // ДОБАВЛЕНО
                            playlistId = playlistId
                        ) as T
                    }
                }
            )
            PlaylistScreen(
                viewModel = playlistViewModel,
                navigateBack = { navController.popBackStack() },
                onTrackClick = { track ->
                    selectedTrack = track
                    navController.navigate(Screen.TRACK_DETAILS.name)
                }
            )
        }

        composable(Screen.SEARCH.name) {
            // Исправлено: убран параметр viewModel, так как SearchScreen создает её сам
            SearchScreen(
                onBackClick = { navController.popBackStack() },
                onTrackClick = {
                    selectedTrack = it
                    navController.navigate(Screen.TRACK_DETAILS.name)
                }
            )
        }

        composable(Screen.NEW_PLAYLIST.name) {
            NewPlaylistScreen(viewModel = allPlaylistsViewModel, onBackClick = { navController.popBackStack() })
        }

        composable(Screen.TRACK_DETAILS.name) {
            selectedTrack?.let {
                TrackDetailsScreen(
                    track = it,
                    viewModel = allPlaylistsViewModel,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }

        composable(Screen.FAVORITES.name) {
            FavoritesScreen(viewModel = allPlaylistsViewModel, onBackClick = { navController.popBackStack() }, onTrackClick = {
                selectedTrack = it
                navController.navigate(Screen.TRACK_DETAILS.name)
            })
        }

        composable(Screen.SETTINGS.name) { SettingsScreen(onBackClick = { navController.popBackStack() }) }
    }
}