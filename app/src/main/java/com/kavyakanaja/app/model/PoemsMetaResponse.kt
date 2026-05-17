package com.kavyakanaja.app.model

import com.google.gson.annotations.SerializedName

/** Response from GET /poems/meta — categories and poets for filter dropdowns */
data class PoemsMetaResponse(
    @SerializedName("categories")  val categories: List<String>,
    @SerializedName("poets")       val poets: List<String>,
    @SerializedName("total_poems") val totalPoems: Int
)