package com.practicum.myapplication.screen

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practicum.myapplication.R
import com.practicum.myapplication.SearchActivity
import com.practicum.myapplication.SettingsActivity

@Composable
fun MainScreen() {
    val context = LocalContext.current

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
                onClick = {
                    context.startActivity(
                        Intent(context, SearchActivity::class.java)
                    )
                }
            )

            MenuItem(
                text = "Плейлисты",
                icon = R.drawable.ic_playlist,
                onClick = { }
            )

            MenuItem(
                text = "Избранное",
                icon = R.drawable.ic_favorite,
                onClick = { }
            )

            MenuItem(
                text = "Настройки",
                icon = R.drawable.ic_settings,
                onClick = {
                    context.startActivity(
                        Intent(context, SettingsActivity::class.java)
                    )
                }
            )
        }
    }
}
