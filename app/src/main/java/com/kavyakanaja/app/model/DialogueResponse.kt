package com.kavyakanaja.app.model

import com.google.gson.annotations.SerializedName

/** Response from POST /dialogue */
data class DialogueResponse(
    @SerializedName("characters")  val characters: String?,
    @SerializedName("dialogue")    val dialogue: String?,
    @SerializedName("setting")     val setting: String?,
    @SerializedName("stage_notes") val stageNotes: String?,
    @SerializedName("theme")       val theme: String?
)