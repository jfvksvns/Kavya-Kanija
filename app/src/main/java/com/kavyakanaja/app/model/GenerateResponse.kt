package com.kavyakanaja.app.model

import com.google.gson.annotations.SerializedName

/** Response from POST /generate */
data class GenerateResponse(
    @SerializedName("poem")         val poem: String?,
    @SerializedName("style_notes")  val styleNotes: String?,
    @SerializedName("poet_profile") val poetProfile: String?,
    @SerializedName("translation")  val translation: String?
)