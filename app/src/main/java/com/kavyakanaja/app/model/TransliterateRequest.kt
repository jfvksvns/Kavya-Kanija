package com.kavyakanaja.app.model

import com.google.gson.annotations.SerializedName

/** Request body for POST /transliterate */
data class TransliterateRequest(
    @SerializedName("text")      val text: String,
    @SerializedName("direction") val direction: String = "kn_to_roman",
    @SerializedName("style")     val style: String = "readable"
)