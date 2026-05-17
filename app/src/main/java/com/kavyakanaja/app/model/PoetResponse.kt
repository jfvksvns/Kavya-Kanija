package com.kavyakanaja.app.model

import com.google.gson.annotations.SerializedName

/** Response from POST /poet */
data class PoetResponse(
    @SerializedName("name")          val name: String?,
    @SerializedName("biography")     val biography: String?,
    @SerializedName("writing_style") val writingStyle: String?,
    @SerializedName("famous_works")  val famousWorks: String?,
    @SerializedName("era")           val era: String?,
    @SerializedName("fun_fact")      val funFact: String?,
    @SerializedName("simple_intro")  val simpleIntro: String?
)