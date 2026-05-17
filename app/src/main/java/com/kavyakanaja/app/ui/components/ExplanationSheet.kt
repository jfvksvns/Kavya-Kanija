package com.kavyakanaja.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kavyakanaja.app.model.DialogueResponse
import com.kavyakanaja.app.model.DifficultyResponse
import com.kavyakanaja.app.model.ExplainResponse
import com.kavyakanaja.app.model.StoryResponse
import com.kavyakanaja.app.model.TransliterateResponse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExplanationSheet(
    explanation: ExplainResponse,
    onPlayAudio: (String) -> Unit,
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
                text = "📖 Poem Explanation",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            explanation.meaning?.let {
                ExplainSection("💡 Simple Meaning", it) { onPlayAudio(it) }
            }

            explanation.lineExplanation?.let {
                ExplainSection("📝 Line by Line", it) { onPlayAudio(it) }
            }

            explanation.wordMeanings?.let {
                ExplainSection("📚 Word Meanings", it) { onPlayAudio(it) }
            }

            explanation.theme?.let {
                ExplainSection("🎨 Theme", it) { onPlayAudio(it) }
            }

            explanation.summary?.let {
                ExplainSection("✨ Summary", it) { onPlayAudio(it) }
            }

            explanation.story?.takeIf { it.isNotBlank() }?.let {
                ExplainSection("📜 Story", it) { onPlayAudio(it) }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Close")
            }
        }
    }
}

// ------------------ STORY ------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StorySheet(
    data: StoryResponse,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(text = data.title ?: "Story", style = MaterialTheme.typography.headlineMedium)

            data.setting?.let { ExplainSection("Setting", it, {}) }
            data.characters?.let { ExplainSection("Characters", it, {}) }
            data.story?.let { ExplainSection("Story", it, {}) }
            data.moral?.let { ExplainSection("Moral", it, {}) }

            Button(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
                Text("Close")
            }
        }
    }
}

// ------------------ DIALOGUE ------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogueSheet(
    data: DialogueResponse,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text("Dialogue", style = MaterialTheme.typography.headlineMedium)

            data.setting?.let { ExplainSection("Setting", it, {}) }
            data.characters?.let { ExplainSection("Characters", it, {}) }
            data.dialogue?.let { ExplainSection("Dialogue", it, {}) }

            Button(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
                Text("Close")
            }
        }
    }
}

// ------------------ DIFFICULTY ------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DifficultySheet(
    data: DifficultyResponse,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text("Difficulty Analysis", style = MaterialTheme.typography.headlineMedium)

            data.reasoning?.let { ExplainSection("Reasoning", it, {}) }
            data.hardWords?.let { ExplainSection("Hard Words", it, {}) }
            data.suggestions?.let { ExplainSection("Tips", it, {}) }

            Button(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
                Text("Close")
            }
        }
    }
}

// ------------------ COMMON SECTION ------------------

@Composable
fun ExplainSection(
    title: String,
    content: String,
    onPlayAudio: () -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold
            )

            IconButton(onClick = onPlayAudio) {
                Icon(Icons.Default.VolumeUp, contentDescription = null)
            }
        }

        Text(text = content)
    }
}