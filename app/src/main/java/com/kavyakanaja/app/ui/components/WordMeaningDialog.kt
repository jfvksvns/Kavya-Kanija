package com.kavyakanaja.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kavyakanaja.app.viewmodel.AiUiState

/**
 * Dialog shown when user taps on a word in the poem.
 * Displays AI-generated meaning of the selected word.
 */
@Composable
fun WordMeaningDialog(
    word: String,
    state: AiUiState,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "\"$word\"",
                style = MaterialTheme.typography.headlineMedium
            )
        },
        text = {
            when (state) {
                is AiUiState.Loading -> LoadingIndicator("Fetching meaning...")
                is AiUiState.WordSuccess -> {
                    Column {
                        state.data.meaning?.let {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
                is AiUiState.Error -> ErrorMessage(state.message)
                else -> {}
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}