package com.kavyakanaja.app.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Coloured difficulty chip — Easy=green / Medium=amber / Hard=red
 * Used on PoemCard and PoemDetailScreen
 */
@Composable
fun DifficultyBadge(difficulty: String) {
    val (label, container, content) = when (difficulty.lowercase()) {
        "easy"   -> Triple("Easy",   Color(0xFFEAF3DE), Color(0xFF27500A))
        "hard"   -> Triple("Hard",   Color(0xFFFCEBEB), Color(0xFF791F1F))
        else     -> Triple("Medium", Color(0xFFFAEEDA), Color(0xFF633806))
    }
    AssistChip(
        onClick = {},
        label = { Text(label, style = MaterialTheme.typography.labelSmall) },
        colors = AssistChipDefaults.assistChipColors(
            containerColor = container,
            labelColor = content
        )
    )
}