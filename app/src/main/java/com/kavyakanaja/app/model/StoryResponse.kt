package com.kavyakanaja.app.model

import com.google.gson.annotations.SerializedName

/** Response from POST /story */
data class StoryResponse(
    @SerializedName("title")      val title: String?,
    @SerializedName("story")      val story: String?,
    @SerializedName("characters") val characters: String?,
    @SerializedName("moral")      val moral: String?,
    @SerializedName("setting")    val setting: String?
)