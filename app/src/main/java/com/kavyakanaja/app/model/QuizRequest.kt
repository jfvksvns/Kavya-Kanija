package com.kavyakanaja.app.model

import com.google.gson.annotations.SerializedName

/** Request body for POST /quiz */
data class QuizRequest(
    @SerializedName("poem_text")      val poemText: String,
    @SerializedName("language")       val language: String = "en",
    @SerializedName("num_questions")  val numQuestions: Int = 5,
    @SerializedName("difficulty")     val difficulty: String = "medium"
)