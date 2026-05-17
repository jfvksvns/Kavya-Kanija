package com.kavyakanaja.app.model

import com.google.gson.annotations.SerializedName

/**
 * AI response containing the structured explanation of a poem or word.
 * All fields are nullable to handle partial responses gracefully.
 */
data class ExplainResponse(
    @SerializedName("meaning") val meaning: String?,
    @SerializedName("line_explanation") val lineExplanation: String?,
    @SerializedName("word_meanings") val wordMeanings: String?,
    @SerializedName("theme") val theme: String?,
    @SerializedName("summary") val summary: String?,
    @SerializedName("story") val story: String?
)