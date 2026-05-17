package com.kavyakanaja.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kavyakanaja.app.ui.components.ErrorMessage
import com.kavyakanaja.app.ui.components.LoadingIndicator
import com.kavyakanaja.app.viewmodel.PoetUiState
import com.kavyakanaja.app.viewmodel.PoetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PoetScreen(
    poetName: String,
    language: String,
    onNavigateBack: () -> Unit,
    viewModel: PoetViewModel = viewModel()
) {
    LaunchedEffect(poetName) { viewModel.loadPoetProfile(poetName, language) }

    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(poetName, style = MaterialTheme.typography.titleMedium) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when (state) {
                is PoetUiState.Loading -> LoadingIndicator("Loading poet profile...")

                is PoetUiState.Error ->
                    ErrorMessage((state as PoetUiState.Error).message) {
                        viewModel.loadPoetProfile(poetName, language)
                    }

                is PoetUiState.Success -> {
                    val poet = (state as PoetUiState.Success).data
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Header card
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        ) {
                            Column(Modifier.padding(20.dp)) {
                                Text(
                                    poet.name ?: poetName,
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                poet.era?.let {
                                    Spacer(Modifier.height(4.dp))
                                    AssistChip(onClick = {}, label = { Text(it) })
                                }
                                poet.simpleIntro?.let {
                                    Spacer(Modifier.height(8.dp))
                                    Text(it, style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer)
                                }
                            }
                        }

                        PoetSection("📖 Biography", poet.biography)
                        PoetSection("✍️ Writing Style", poet.writingStyle)
                        PoetSection("📚 Famous Works", poet.famousWorks)
                        PoetSection("💡 Fun Fact", poet.funFact)
                    }
                }

                else -> {}
            }
        }
    }
}

@Composable
private fun PoetSection(title: String, content: String?) {
    content?.takeIf { it.isNotBlank() } ?: return
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.height(8.dp))
            Text(content, style = MaterialTheme.typography.bodyLarge)
        }
    }
}