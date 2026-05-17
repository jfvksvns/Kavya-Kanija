package com.kavyakanaja.app.model

import com.google.gson.annotations.SerializedName

/** Request body for POST /generate */
data class GenerateRequest(
    @SerializedName("poet_name") val poetName: String,
    @SerializedName("topic")     val topic: String,
    @SerializedName("language")  val language: String = "en",
    @SerializedName("length")    val length: String = "short"
)