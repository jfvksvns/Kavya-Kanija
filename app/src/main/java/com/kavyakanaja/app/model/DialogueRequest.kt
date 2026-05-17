package com.kavyakanaja.app.model

import com.google.gson.annotations.SerializedName

/** Request body for POST /dialogue */
data class DialogueRequest(
    @SerializedName("poem_text")       val poemText: String,
    @SerializedName("language")        val language: String = "en",
    @SerializedName("num_characters")  val numCharacters: Int = 2
)