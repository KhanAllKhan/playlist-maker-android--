package com.practicum.myapplication.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.practicum.myapplication.R
import com.practicum.myapplication.domain.model.SearchState
import com.practicum.myapplication.domain.model.Track
import com.practicum.myapplication.ui.components.TrackListItem
import com.practicum.myapplication.ui.viewmodel.SearchViewModel

@Composable
fun SearchScreen(
    onBackClick: () -> Unit = {},
    onTrackClick: (Track) -> Unit = {}
) {
    // 1. Получаем контекст приложения для работы Creator (БД и DataStore)
    val context = LocalContext.current.applicationContext

    // 2. Инициализируем ViewModel через фабрику, передавая контекст
    val viewModel: SearchViewModel = viewModel(
        factory = SearchViewModel.getViewModelFactory(context)
    )

    var searchText by remember { mutableStateOf("") }

    // Подписка на состояния из ViewModel
    val screenState by viewModel.searchScreenState.collectAsState()
    val history by viewModel.historyState.collectAsState()

    val focusManager = LocalFocusManager.current

    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        // --- ВЕРХНЯЯ ПАНЕЛЬ ---
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
            Text(text = "Поиск", color = Color.White, fontSize = 20.sp)
        }

        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            // --- ПОЛЕ ВВОДА ---
            OutlinedTextField(
                value = searchText,
                onValueChange = {
                    searchText = it
                    if (it.isNotEmpty()) {
                        viewModel.search(it) // Выполняем поиск и сохраняем в историю
                    } else {
                        viewModel.clearSearch() // Очищаем и подгружаем историю[cite: 1]
                    }
                },
                placeholder = { Text("Введите название трека") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (searchText.isNotEmpty()) {
                        IconButton(onClick = {
                            searchText = ""
                            viewModel.clearSearch()
                            focusManager.clearFocus()
                        }) { Icon(Icons.Default.Clear, contentDescription = null) }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // --- КОНТЕНТ (ИСТОРИЯ ИЛИ РЕЗУЛЬТАТЫ) ---
            if (searchText.isEmpty() && history.isNotEmpty()) {
                // ВИЗУАЛЬНАЯ ЧАСТЬ ИСТОРИИ (Как на макете)[cite: 1]
                Text(
                    text = "Вы искали",
                    modifier = Modifier.padding(bottom = 12.dp, top = 8.dp),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(history) { query ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    searchText = query
                                    viewModel.search(query)
                                }
                                .padding(vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                // Убедись, что у тебя есть иконка ic_history (часики) в drawable
                                painter = painterResource(id = R.drawable.ic_history),
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(12.dp))
                            Text(text = query, fontSize = 16.sp, color = Color.Black)
                        }
                    }
                }
            } else {
                // ОТОБРАЖЕНИЕ СОСТОЯНИЙ ПОИСКА[cite: 1]
                when (screenState) {
                    is SearchState.Loading -> {
                        Box(Modifier.fillMaxSize(), Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                    is SearchState.Error -> {
                        Box(Modifier.fillMaxSize(), Alignment.Center) {
                            Text(
                                text = (screenState as SearchState.Error).error,
                                color = Color.Red
                            )
                        }
                    }
                    is SearchState.Success -> {
                        val tracks = (screenState as SearchState.Success).foundList
                        LazyColumn(Modifier.fillMaxSize()) {
                            items(tracks) { track ->
                                TrackListItem(
                                    track = track,
                                    onClick = { onTrackClick(track) }
                                )
                                HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)
                            }
                        }
                    }
                    else -> {}
                }
            }
        }
    }
}