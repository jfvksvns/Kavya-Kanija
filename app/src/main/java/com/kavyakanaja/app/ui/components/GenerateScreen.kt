package com.kavyakanaja.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kavyakanaja.app.ui.components.ErrorMessage
import com.kavyakanaja.app.ui.components.LoadingIndicator
import com.kavyakanaja.app.utils.Constants
import com.kavyakanaja.app.viewmodel.GenerateUiState
import com.kavyakanaja.app.viewmodel.GenerateViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenerateScreen(
    language: String,
    onNavigateBack: () -> Unit,
    viewModel: GenerateViewModel = viewModel()
) {
    val state    by viewModel.state.collectAsStateWithLifecycle()
    val poetName by viewModel.poetName.collectAsStateWithLifecycle()
    val topic    by viewModel.topic.collectAsStateWithLifecycle()
    val length   by viewModel.length.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Generate Poem") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("✨ Write Like a Poet",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary)

            OutlinedTextField(
                value = poetName,
                onValueChange = { viewModel.onPoetNameChange(it) },
                label = { Text("Poet Name (e.g. Kuvempu, DVG)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = topic,
                onValueChange = { viewModel.onTopicChange(it) },
                label = { Text("Topic (e.g. Nature, Friendship)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Length selector
            Text("Poem Length", style = MaterialTheme.typography.titleMedium)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(
                    Constants.LENGTH_SHORT  to "Short",
                    Constants.LENGTH_MEDIUM to "Medium",
                    Constants.LENGTH_LONG   to "Long"
                ).forEach { (value, label) ->
                    FilterChip(
                        selected = length == value,
                        onClick = { viewModel.onLengthChange(value) },
                        label = { Text(label) }
                    )
                }
            }

            Button(
                onClick = { viewModel.generate(language) },
                modifier = Modifier.fillMaxWidth(),
                enabled = poetName.isNotBlank() && topic.isNotBlank()
                        && state !is GenerateUiState.Loading
            ) {
                Text("Generate Poem")
            }

            // Result
            when (state) {
                is GenerateUiState.Loading ->
                    LoadingIndicator("Writing poem in poet's style...")

                is GenerateUiState.Error ->
                    ErrorMessage((state as GenerateUiState.Error).message)

                is GenerateUiState.Success -> {
                    val result = (state as GenerateUiState.Success).data

                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Column(Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Text("🪷 Generated Poem",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary)
                            Text(result.poem ?: "",
                                style = MaterialTheme.typography.bodyLarge)

                            result.translation?.takeIf { it.isNotBlank() }?.let {
                                HorizontalDivider()
                                Text("Translation", style = MaterialTheme.typography.titleMedium)
                                Text(it, style = MaterialTheme.typography.bodyMedium)
                            }

                            result.styleNotes?.takeIf { it.isNotBlank() }?.let {
                                HorizontalDivider()
                                Text("Style Notes", style = MaterialTheme.typography.titleMedium)
                                Text(it, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }

                    OutlinedButton(
                        onClick = { viewModel.reset() },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("Generate Another") }
                }

                else -> {}
            }
        }
    }
}