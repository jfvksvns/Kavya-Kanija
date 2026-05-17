package com.kavyakanaja.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kavyakanaja.app.model.TransliterateResponse

/**
 * Bottom sheet showing Kannada original + Roman transliteration
 * side by side with a pronunciation guide.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransliterateSheet(
    data: TransliterateResponse,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                "🔤 Transliteration",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(16.dp))

            // Original Kannada
            Text("Original (Kannada)",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.height(6.dp))
            Text(
                data.original ?: "",
                style = MaterialTheme.typography.bodyLarge
            )

            HorizontalDivider(Modifier.padding(vertical = 14.dp))

            // Transliterated Roman
            Text("Transliterated (Roman)",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.height(6.dp))
            Text(
                data.transliterated ?: "",
                style = MaterialTheme.typography.bodyLarge
            )

            // Pronunciation guide
            data.pronunciationGuide?.let { guide ->
                HorizontalDivider(Modifier.padding(vertical = 14.dp))
                Text("🗣️ Pronunciation Guide",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.height(6.dp))
                Text(guide, style = MaterialTheme.typography.bodyMedium)
            }

            Spacer(Modifier.height(16.dp))
            Button(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
                Text("Close")
            }
        }
    }
}