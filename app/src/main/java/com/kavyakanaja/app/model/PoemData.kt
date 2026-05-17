package com.kavyakanaja.app.model

import com.google.gson.annotations.SerializedName

/**
 * Poem model from GET /poems API.
 * Replaces the old local Poem data class.
 */
data class PoemData(
    @SerializedName("id")          val id: Int,
    @SerializedName("title_en")    val titleEn: String,
    @SerializedName("title_kn")    val titleKn: String,
    @SerializedName("poet")        val poet: String,
    @SerializedName("text")        val text: String,
    @SerializedName("category")    val category: String,
    @SerializedName("difficulty")  val difficulty: String,
    @SerializedName("is_featured") val isFeatured: Boolean,
    @SerializedName("tags")        val tags: List<String>,
    @SerializedName("era")         val era: String
)