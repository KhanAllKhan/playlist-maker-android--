package com.practicum.myapplication.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.practicum.myapplication.R
import com.practicum.myapplication.creator.Creator
import com.practicum.myapplication.domain.model.SearchState
import com.practicum.myapplication.ui.components.TrackListItem
import com.practicum.myapplication.ui.viewmodel.TracksViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllTracksScreen(
    onBackClick: () -> Unit = {},
    viewModel: TracksViewModel = viewModel(
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return TracksViewModel(Creator.getTracksRepository()) as T
            }
        }
    )
) {
    // Получение состояния экрана из ViewModel[cite: 1]
    val screenState by viewModel.allTracksScreenState.collectAsState()

    // Состояние для управления показом всплывающей панели
    var showBottomSheet by remember { mutableStateOf(false) }

    // Загрузка данных при первом запуске[cite: 1]
    LaunchedEffect(Unit) {
        viewModel.fetchAllTracks()
    }

    // Использование Scaffold для удобного размещения FloatingActionButton
    Scaffold(
        floatingActionButton = {
            // Реализация плавающей кнопки из задания
            FloatingActionButton(
                modifier = Modifier
                    .padding(32.dp),
                onClick = { showBottomSheet = true }, // Открытие панели при нажатии
                containerColor = Color.Gray,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "This is FloatingActionButton"
                )
            }
        }
    ) { innerPadding ->
        // Основной контейнер экрана[cite: 1]
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White)
        ) {
            // Верхняя панель навигации[cite: 1]
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
                        contentDescription = "Назад",
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Все треки",
                    color = Color.White,
                    fontSize = 20.sp
                )
            }

            // Логика отображения контента в зависимости от состояния[cite: 1]
            when (screenState) {
                is SearchState.Initial -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Начните поиск", color = Color.Gray)
                    }
                }

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
                                    modifier = Modifier.padding(vertical = 12.dp)
                                )
                                Divider(
                                    thickness = 0.5.dp,
                                    color = Color.LightGray,
                                    modifier = Modifier.padding(top = 8.dp)
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
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Ошибка",
                                color = Color.Red,
                                fontSize = 16.sp
                            )
                            Text(
                                text = (screenState as SearchState.Error).error,
                                color = Color.Gray,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }
            }
        }

        // Реализация всплывающей панели ModalBottomSheet из задания
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false }, // Закрытие при клике вне панели
                sheetState = rememberModalBottomSheetState()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 64.dp, start = 16.dp, end = 16.dp, top = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Это панель BottomSheet",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp
                    )
                    Icon(
                        modifier = Modifier
                            .size(48.dp)
                            .padding(top = 16.dp),
                        imageVector = Icons.Default.Home,
                        contentDescription = "Icon"
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTrackListItem() {
    val track = com.practicum.myapplication.domain.model.Track(
        trackName = "Владивосток 2000",
        artistName = "Мумий Троль",
        trackTime = "2:38"
    )
    TrackListItem(track = track)
}