package com.kavyakanaja.app.model

import com.google.gson.annotations.SerializedName

/** Request body for POST /story */
data class StoryRequest(
    @SerializedName("poem_text") val poemText: String,
    @SerializedName("language")  val language: String = "en",
    @SerializedName("style")     val style: String = "simple"
)