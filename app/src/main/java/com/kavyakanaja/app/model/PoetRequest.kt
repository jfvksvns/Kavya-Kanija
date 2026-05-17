package com.kavyakanaja.app.model

import com.google.gson.annotations.SerializedName

/** Request body for POST /poet */
data class PoetRequest(
    @SerializedName("poet_name") val poetName: String,
    @SerializedName("language")  val language: String = "en"
)