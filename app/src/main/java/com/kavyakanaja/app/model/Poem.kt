package com.kavyakanaja.app.model

/**
 * Represents a Kannada poem in the app.
 * In a real app, this would come from a remote API or local JSON asset.
 */
data class Poem(
    val id: Int,
    val titleEn: String,         // Title in English
    val titleKn: String,         // Title in Kannada
    val poet: String,            // Poet name
    val text: String,            // Full poem text (Kannada)
    val category: String = "",   // e.g., "Nature", "Devotion"
    val isFeatured: Boolean = false  // For "Daily Poem" section
)