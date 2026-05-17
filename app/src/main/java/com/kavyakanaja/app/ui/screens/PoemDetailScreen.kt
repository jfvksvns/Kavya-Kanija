package com.kavyakanaja.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kavyakanaja.app.ui.components.*
import com.kavyakanaja.app.viewmodel.AiUiState
import com.kavyakanaja.app.viewmodel.PoemDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PoemDetailScreen(
    poemId: Int,
    language: String,
    onNavigateBack: () -> Unit,
    onPoetClick: (String) -> Unit,
    onQuizClick: (Int, String) -> Unit,
    viewModel: PoemDetailViewModel = viewModel()
) {
    LaunchedEffect(poemId) { viewModel.loadPoem(poemId, language) }

    val poem        by viewModel.poem.collectAsStateWithLifecycle()
    val isFavorite  by viewModel.isFavorite.collectAsStateWithLifecycle()
    val aiState     by viewModel.aiState.collectAsStateWithLifecycle()
    val wordState   by viewModel.wordState.collectAsStateWithLifecycle()
    val isSpeaking  by viewModel.isSpeaking.collectAsStateWithLifecycle()
    val ttsReady    by viewModel.ttsReady.collectAsStateWithLifecycle()
    val relatedPoems by viewModel.relatedPoems.collectAsStateWithLifecycle()

    var selectedWord by remember { mutableStateOf("") }
    var showStylePicker by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (language == "kn") poem?.titleKn ?: ""
                        else poem?.titleEn ?: "",
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.toggleFavorite() }) {
                        Icon(
                            if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favourite",
                            tint = if (isFavorite) MaterialTheme.colorScheme.secondary
                            else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        }
    ) { padding ->
        poem?.let { currentPoem ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
            ) {
                // ── Header ────────────────────────────────────────────────
                Card(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(Modifier.padding(20.dp)) {
                        Text(
                            if (language == "kn") currentPoem.titleKn else currentPoem.titleEn,
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(Modifier.height(4.dp))

                        // Clickable poet name → navigate to PoetScreen
                        TextButton(
                            onClick = { onPoetClick(currentPoem.poet) },
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(
                                "— ${currentPoem.poet}",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontStyle = FontStyle.Italic
                                ),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Spacer(Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            AssistChip(onClick = {}, label = {
                                Text(currentPoem.category,
                                    style = MaterialTheme.typography.labelSmall)
                            })
                            AssistChip(onClick = {}, label = {
                                Text(currentPoem.era,
                                    style = MaterialTheme.typography.labelSmall)
                            })
                        }
                    }
                }

                // ── Poem text (clickable words) ────────────────────────────
                Box(Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp)) {
                    val words = currentPoem.text.split(" ")
                    val annotatedText = buildAnnotatedString {
                        words.forEachIndexed { index, word ->
                            pushStringAnnotation("WORD", word)
                            withStyle(SpanStyle(color = MaterialTheme.colorScheme.onSurface)) {
                                append(word)
                            }
                            pop()
                            if (index < words.lastIndex) append(" ")
                        }
                    }
                    ClickableText(
                        text = annotatedText,
                        style = MaterialTheme.typography.bodyLarge,
                        onClick = { offset ->
                            annotatedText
                                .getStringAnnotations("WORD", offset, offset)
                                .firstOrNull()?.let {
                                    val clean = it.item.replace("[,।॥]".toRegex(), "")
                                    if (clean.isNotBlank()) {
                                        selectedWord = clean
                                        viewModel.explainWord(clean)
                                    }
                                }
                        }
                    )
                }

                Text(
                    "💡 Tap any word to see its meaning",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(0.5f),
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
                )

                HorizontalDivider(Modifier.padding(horizontal = 16.dp, vertical = 12.dp))

                // ── Primary actions ───────────────────────────────────────
                Row(
                    Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = { viewModel.explainPoem() },
                        modifier = Modifier.weight(1f),
                        enabled = aiState !is AiUiState.Loading
                    ) {
                        if (aiState is AiUiState.Loading)
                            CircularProgressIndicator(Modifier.size(16.dp), strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.onPrimary)
                        else Icon(Icons.Default.AutoAwesome, null)
                        Spacer(Modifier.width(6.dp))
                        Text("Explain")
                    }
                    OutlinedButton(
                        onClick = { viewModel.playPoemAudio() },
                        modifier = Modifier.weight(1f),
                        enabled = ttsReady
                    ) {
                        Icon(
                            if (isSpeaking) Icons.Default.Stop else Icons.Default.VolumeUp,
                            null
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(if (isSpeaking) "Stop" else "Listen")
                    }
                }

                Spacer(Modifier.height(8.dp))

                // ── Group A feature buttons ───────────────────────────────
                Column(
                    Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Explore", style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(0.6f))

                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FeatureButton("😔 Emotion",  Modifier.weight(1f)) { viewModel.detectEmotion() }
                        FeatureButton("💡 Lesson",   Modifier.weight(1f)) { viewModel.extractLesson() }
                    }
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FeatureButton("🧒 Simplify", Modifier.weight(1f)) { viewModel.simplifyPoem() }
                        FeatureButton("🔄 Modernize",Modifier.weight(1f)) { viewModel.modernizePoem() }
                    }
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FeatureButton("🎨 Visualize",Modifier.weight(1f)) { viewModel.visualizePoem() }
                        FeatureButton("📜 History",  Modifier.weight(1f)) { viewModel.getBackground() }
                    }
                }

                Spacer(Modifier.height(8.dp))

                // ── Group B feature buttons ───────────────────────────────
                Column(
                    Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Create", style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(0.6f))

                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FeatureButton("📖 Story",   Modifier.weight(1f)) { showStylePicker = true }
                        FeatureButton("🎭 Dialogue",Modifier.weight(1f)) { viewModel.generateDialogue() }
                    }
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FeatureButton("🔤 Transliterate", Modifier.weight(1f)) { viewModel.transliterate() }
                        FeatureButton("📊 Difficulty",    Modifier.weight(1f)) { viewModel.scoreDifficulty() }
                    }
                    // Quiz button — full width
                    OutlinedButton(
                        onClick = { onQuizClick(currentPoem.id, currentPoem.text) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Quiz, null)
                        Spacer(Modifier.width(8.dp))
                        Text("🧠 Take Quiz")
                    }
                }

                // ── AI error message ──────────────────────────────────────
                if (aiState is AiUiState.Error) {
                    ErrorMessage(
                        (aiState as AiUiState.Error).message,
                        onRetry = { viewModel.resetAiState() }
                    )
                }

                // ── Related poems ─────────────────────────────────────────
                if (relatedPoems.isNotEmpty()) {
                    Spacer(Modifier.height(16.dp))
                    Text(
                        "Related Poems",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    relatedPoems.forEach { related ->
                        PoemCard(
                            poem = related,
                            language = language,
                            onClick = { /* navigate */ }
                        )
                    }
                }

                Spacer(Modifier.height(32.dp))
            }
        } ?: Box(Modifier.fillMaxSize(), Alignment.Center) {
            CircularProgressIndicator()
        }
    }

    // ── AI Result Sheets ──────────────────────────────────────────────────
    when (aiState) {
        is AiUiState.ExplainSuccess -> ExplanationSheet(
            explanation = (aiState as AiUiState.ExplainSuccess).data,
            onPlayAudio = { viewModel.speakText(it) },
            onDismiss   = { viewModel.resetAiState() }
        )
        is AiUiState.StorySuccess -> StorySheet(
            data      = (aiState as AiUiState.StorySuccess).data,
            onDismiss = { viewModel.resetAiState() }
        )
        is AiUiState.DialogueSuccess -> DialogueSheet(
            data      = (aiState as AiUiState.DialogueSuccess).data,
            onDismiss = { viewModel.resetAiState() }
        )
        is AiUiState.DifficultySuccess -> DifficultySheet(
            data      = (aiState as AiUiState.DifficultySuccess).data,
            onDismiss = { viewModel.resetAiState() }
        )
        is AiUiState.TranslitSuccess -> TransliterateSheet(
            data      = (aiState as AiUiState.TranslitSuccess).data,
            onDismiss = { viewModel.resetAiState() }
        )
        else -> {}
    }

    // Word meaning dialog
    if (wordState !is AiUiState.Idle && selectedWord.isNotEmpty()) {
        WordMeaningDialog(
            word      = selectedWord,
            state     = wordState,
            onDismiss = { selectedWord = ""; viewModel.clearWordState() }
        )
    }

    // Story style picker dialog
    if (showStylePicker) {
        AlertDialog(
            onDismissRequest = { showStylePicker = false },
            title = { Text("Choose Story Style") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("simple" to "Simple", "dramatic" to "Dramatic", "children" to "For Kids")
                        .forEach { (value, label) ->
                            OutlinedButton(
                                onClick = {
                                    showStylePicker = false
                                    viewModel.generateStory(value)
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) { Text(label) }
                        }
                }
            },
            confirmButton = {
                TextButton(onClick = { showStylePicker = false }) { Text("Cancel") }
            }
        )
    }
}

// Small helper composable for feature buttons
@Composable
private fun FeatureButton(label: String, modifier: Modifier, onClick: () -> Unit) {
    OutlinedButton(onClick = onClick, modifier = modifier) {
        Text(label, style = MaterialTheme.typography.labelSmall)
    }
}
