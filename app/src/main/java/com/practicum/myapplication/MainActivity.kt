package com.practicum.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.practicum.myapplication.navigation.PlaylistHost
import com.practicum.myapplication.ui.themes.MyApplicationTheme
import com.practicum.myapplication.ui.viewmodel.AllPlaylistsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()

                    val allPlaylistsViewModel: AllPlaylistsViewModel = viewModel(
                        factory = AllPlaylistsViewModel.getViewModelFactory(applicationContext)
                    )

                    PlaylistHost(
                        navController = navController,
                        allPlaylistsViewModel = allPlaylistsViewModel
                    )
                }
            }
        }
    }
}