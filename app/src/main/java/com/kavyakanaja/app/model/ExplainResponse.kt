package com.kavyakanaja.app.model

import com.google.gson.annotations.SerializedName

/**
 * Request body sent to the AI explanation API.
 * @param text The poem text or word to explain
 * @param language "en" for English, "kn" for Kannada
 * @param mode "full" for poem explanation, "word" for single word meaning
 */
data class ExplainRequest(
    @SerializedName("text") val text: String,
    @SerializedName("language") val language: String,
    @SerializedName("mode") val mode: String
)