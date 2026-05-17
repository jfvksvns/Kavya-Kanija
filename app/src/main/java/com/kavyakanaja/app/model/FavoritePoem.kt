package com.kavyakanaja.app.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity representing a poem saved by the user as a favorite.
 */
@Entity(tableName = "favorite_poems")
data class FavoritePoem(
    @PrimaryKey val poemId: Int,
    val titleEn: String,
    val titleKn: String,
    val poet: String,
    val text: String,
    val savedAt: Long = System.currentTimeMillis()
)