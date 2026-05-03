package com.practicum.myapplication.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practicum.myapplication.domain.model.Track

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TrackListItem(
    track: Track,
    showDivider: Boolean = false,
    modifier: Modifier = Modifier,
    onClick: (Track) -> Unit,
    onLongClick: ((Track) -> Unit)? = null // ДОБАВЛЕНО: Обработчик долгого нажатия
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            // ИСПРАВЛЕНО: Используем combinedClickable для поддержки onClick и onLongClick
            .combinedClickable(
                onClick = { onClick(track) },
                onLongClick = { onLongClick?.invoke(track) }
            )
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = track.trackName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = track.artistName,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            Text(
                text = track.trackTime,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }

        if (showDivider) {
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)
        }
    }
}