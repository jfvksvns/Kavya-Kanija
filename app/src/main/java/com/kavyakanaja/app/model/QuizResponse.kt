package com.kavyakanaja.app.model

import com.google.gson.annotations.SerializedName

/** Response from POST /quiz */
data class QuizResponse(
    @SerializedName("mcq_questions") val mcqQuestions: List<QuizQuestion>?,
    @SerializedName("tf_questions")  val tfQuestions: List<TrueFalseQuestion>?,
    @SerializedName("poem_summary")  val poemSummary: String?,
    @SerializedName("difficulty")    val difficulty: String?
)

data class QuizQuestion(
    @SerializedName("question")       val question: String,
    @SerializedName("option_a")       val optionA: String,
    @SerializedName("option_b")       val optionB: String,
    @SerializedName("option_c")       val optionC: String,
    @SerializedName("option_d")       val optionD: String,
    @SerializedName("correct_answer") val correctAnswer: String,
    @SerializedName("explanation")    val explanation: String
)

data class TrueFalseQuestion(
    @SerializedName("statement")   val statement: String,
    @SerializedName("answer")      val answer: Boolean,
    @SerializedName("explanation") val explanation: String
)