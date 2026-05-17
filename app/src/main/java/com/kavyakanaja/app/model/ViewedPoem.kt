package com.kavyakanaja.app.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity for tracking recently viewed poems (History feature).
 */
@Entity(tableName = "viewed_poems")
data class ViewedPoem(
    @PrimaryKey val poemId: Int,
    val titleEn: String,
    val titleKn: String,
    val poet: String,
    val text: String,
    val viewedAt: Long = System.currentTimeMillis()
)