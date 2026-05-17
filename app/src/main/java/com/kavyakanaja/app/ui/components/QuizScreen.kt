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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kavyakanaja.app.model.QuizQuestion
import com.kavyakanaja.app.ui.components.ErrorMessage
import com.kavyakanaja.app.ui.components.LoadingIndicator
import com.kavyakanaja.app.viewmodel.QuizUiState
import com.kavyakanaja.app.viewmodel.QuizViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    poemId: Int,
    poemText: String,
    language: String,
    onNavigateBack: () -> Unit,
    viewModel: QuizViewModel = viewModel()
) {
    LaunchedEffect(poemId) { viewModel.loadQuiz(poemText, language) }

    val state          by viewModel.state.collectAsStateWithLifecycle()
    val currentIndex   by viewModel.currentMcqIndex.collectAsStateWithLifecycle()
    val selectedAnswers by viewModel.selectedAnswers.collectAsStateWithLifecycle()
    val quizFinished   by viewModel.quizFinished.collectAsStateWithLifecycle()
    val score          by viewModel.score.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quiz") },
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
                is QuizUiState.Loading ->
                    LoadingIndicator("Generating quiz questions...")

                is QuizUiState.Error ->
                    ErrorMessage((state as QuizUiState.Error).message) {
                        viewModel.loadQuiz(poemText, language)
                    }

                is QuizUiState.Success -> {
                    val quiz = (state as QuizUiState.Success).data
                    val questions = quiz.mcqQuestions ?: emptyList()

                    if (quizFinished) {
                        // ── Results screen ────────────────────────────────
                        QuizResultScreen(
                            score = score,
                            total = questions.size,
                            onRetry = { viewModel.loadQuiz(poemText, language) },
                            onBack = onNavigateBack
                        )
                    } else if (questions.isNotEmpty()) {
                        // ── Quiz question screen ──────────────────────────
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // Progress
                            LinearProgressIndicator(
                                progress = { (currentIndex + 1f) / questions.size },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Text(
                                "Question ${currentIndex + 1} of ${questions.size}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(0.6f)
                            )

                            // Question card
                            McqQuestionCard(
                                question = questions[currentIndex],
                                questionIndex = currentIndex,
                                selectedAnswer = selectedAnswers[currentIndex],
                                onAnswerSelected = { viewModel.selectAnswer(currentIndex, it) }
                            )

                            // Next button
                            Button(
                                onClick = { viewModel.nextQuestion(questions.size) },
                                modifier = Modifier.fillMaxWidth(),
                                enabled = selectedAnswers.containsKey(currentIndex)
                            ) {
                                Text(if (currentIndex == questions.lastIndex) "See Results" else "Next")
                            }
                        }
                    }
                }

                else -> {}
            }
        }
    }
}

@Composable
private fun McqQuestionCard(
    question: QuizQuestion,
    questionIndex: Int,
    selectedAnswer: String?,
    onAnswerSelected: (String) -> Unit
) {
    val options = listOf(
        "A" to question.optionA,
        "B" to question.optionB,
        "C" to question.optionC,
        "D" to question.optionD
    )

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(question.question, style = MaterialTheme.typography.titleMedium)
            HorizontalDivider()
            options.forEach { (key, text) ->
                val isSelected = selectedAnswer == key
                OutlinedButton(
                    onClick = { onAnswerSelected(key) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (isSelected)
                            MaterialTheme.colorScheme.primaryContainer
                        else Color.Transparent
                    )
                ) {
                    Text("$key. $text", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

@Composable
private fun QuizResultScreen(
    score: Int,
    total: Int,
    onRetry: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("🎉", style = MaterialTheme.typography.displayMedium)
        Spacer(Modifier.height(16.dp))
        Text("Quiz Complete!", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(8.dp))
        Text(
            "$score / $total correct",
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(8.dp))
        val pct = if (total > 0) (score * 100) / total else 0
        Text(
            when {
                pct >= 80 -> "Excellent! You know this poem well 🌟"
                pct >= 50 -> "Good effort! Keep reading 📖"
                else      -> "Keep learning — you'll get there! 💪"
            },
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(Modifier.height(32.dp))
        Button(onClick = onRetry, modifier = Modifier.fillMaxWidth()) { Text("Try Again") }
        Spacer(Modifier.height(8.dp))
        OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text("Back to Poem") }
    }
}