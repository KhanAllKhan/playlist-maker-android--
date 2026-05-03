package com.practicum.myapplication.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practicum.myapplication.R

@Composable
fun MainScreen(
    onSearchClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onPlaylistsClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF4B74E6))
    ) {

        Text(
            text = "Playlist maker",
            color = Color.White,
            fontSize = 22.sp,
            modifier = Modifier.padding(16.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                )
                .padding(vertical = 16.dp)
        ) {

            MenuItem(
                text = "Поиск",
                icon = R.drawable.ic_search,
                onClick = onSearchClick
            )

            MenuItem(
                text = "Плейлисты",
                icon = R.drawable.ic_playlist,
                onClick = onPlaylistsClick
            )

            MenuItem(
                text = "Избранное",
                icon = R.drawable.ic_favorite,
                onClick = { /* TODO: Добавить навигацию к избранному */ }
            )

            MenuItem(
                text = "Настройки",
                icon = R.drawable.ic_settings,
                onClick = onSettingsClick
            )
        }
    }
}