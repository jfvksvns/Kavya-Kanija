package com.kavyakanaja.app.model

import com.google.gson.annotations.SerializedName

/** Response from POST /transliterate */
data class TransliterateResponse(
    @SerializedName("original")             val original: String?,
    @SerializedName("transliterated")       val transliterated: String?,
    @SerializedName("direction")            val direction: String?,
    @SerializedName("pronunciation_guide")  val pronunciationGuide: String?
)