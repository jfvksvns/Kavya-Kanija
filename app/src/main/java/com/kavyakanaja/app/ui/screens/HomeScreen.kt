package com.kavyakanaja.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

import com.kavyakanaja.app.R
import com.kavyakanaja.app.ui.components.PoemCard
import com.kavyakanaja.app.viewmodel.HomeViewModel
import com.kavyakanaja.app.model.Poem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onPoemClick: (Int) -> Unit,
    onFavoritesClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onGenerateClick: () -> Unit,

    viewModel: HomeViewModel = viewModel()
) {
    val poems by viewModel.poems.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val language by viewModel.selectedLanguage.collectAsStateWithLifecycle()
    val dailyPoemData by viewModel.dailyPoem.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                actions = {
                    IconButton(onClick = { viewModel.toggleLanguage() }) {
                        Text(
                            text = if (language == "en") "ಕನ್ನಡ" else "EN",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    IconButton(onClick = onHistoryClick) {
                        Icon(Icons.Default.History, contentDescription = "History")
                    }

                    IconButton(onClick = onFavoritesClick) {
                        Icon(Icons.Default.Favorite, contentDescription = "Favorites")
                    }

                    IconButton(onClick = onGenerateClick) {
                        Icon(Icons.Default.Create, contentDescription = "Generate")
                    }
                }
            )
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // 🔍 Search Bar
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.onSearchQueryChange(it) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(stringResource(R.string.search_hint))
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null)
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = {
                                viewModel.onSearchQueryChange("")
                            }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear")
                            }
                        }
                    },
                    singleLine = true
                )
            }

            // 🌟 Daily Poem (FIXED)
            dailyPoemData?.let { data ->

                val poem = Poem(
                    id = data.id,
                    titleEn = data.titleEn,
                    titleKn = data.titleKn,
                    poet = data.poet,
                    text = data.text
                )

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { onPoemClick(poem.id) }
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {

                            Text(
                                text = if (language == "kn") poem.titleKn else poem.titleEn,
                                style = MaterialTheme.typography.headlineMedium
                            )

                            Text(
                                text = poem.poet,
                                style = MaterialTheme.typography.bodyMedium
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = poem.text,
                                maxLines = 3,
                                overflow = TextOverflow.Ellipsis,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontStyle = FontStyle.Italic
                                )
                            )
                        }
                    }
                }
            }

            // 📌 Header
            item {
                Text(
                    text = stringResource(R.string.all_poems),
                    style = MaterialTheme.typography.titleMedium
                )
            }

            // 📜 List
            items(poems, key = { it.id }) { poem ->
                PoemCard(
                    poem = poem,
                    language = language,
                    onClick = { onPoemClick(poem.id) }
                )
            }
        }
    }
}