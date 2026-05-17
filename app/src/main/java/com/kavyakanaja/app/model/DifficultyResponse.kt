package com.kavyakanaja.app.model

import com.google.gson.annotations.SerializedName

/** Response from POST /difficulty */
data class DifficultyResponse(
    @SerializedName("level")       val level: String?,
    @SerializedName("score")       val score: Int?,
    @SerializedName("reasoning")   val reasoning: String?,
    @SerializedName("hard_words")  val hardWords: String?,
    @SerializedName("suggestions") val suggestions: String?
)