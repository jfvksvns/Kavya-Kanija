package com.kavyakanaja.app.data.repository

import com.kavyakanaja.app.data.api.ApiService
import com.kavyakanaja.app.model.*

/**
 * Repository for all AI-powered explanation endpoints.
 * UPDATED: Added story, poet, quiz, dialogue, generate,
 *          difficulty, transliterate.
 */
class ExplainRepository(private val apiService: ApiService) {

    // ── Original /explain ─────────────────────────────────────────────────

    suspend fun getExplanation(
        text: String,
        language: String,
        mode: String
    ): ExplainResult {
        return safeCall {
            apiService.explainPoem(ExplainRequest(text, language, mode))
        }
    }

    // ── Group B ───────────────────────────────────────────────────────────

    suspend fun generateStory(
        poemText: String,
        language: String,
        style: String
    ): StoryResult {
        return try {
            val response = apiService.generateStory(
                StoryRequest(poemText, language, style)
            )
            if (response.isSuccessful && response.body() != null)
                StoryResult.Success(response.body()!!)
            else StoryResult.Error("Server error: ${response.code()}")
        } catch (e: Exception) {
            StoryResult.Error(e.localizedMessage ?: "Network error")
        }
    }

    suspend fun getPoetProfile(
        poetName: String,
        language: String
    ): PoetResult {
        return try {
            val response = apiService.getPoetProfile(PoetRequest(poetName, language))
            if (response.isSuccessful && response.body() != null)
                PoetResult.Success(response.body()!!)
            else PoetResult.Error("Server error: ${response.code()}")
        } catch (e: Exception) {
            PoetResult.Error(e.localizedMessage ?: "Network error")
        }
    }

    suspend fun generateQuiz(
        poemText: String,
        language: String,
        numQuestions: Int,
        difficulty: String
    ): QuizResult {
        return try {
            val response = apiService.generateQuiz(
                QuizRequest(poemText, language, numQuestions, difficulty)
            )
            if (response.isSuccessful && response.body() != null)
                QuizResult.Success(response.body()!!)
            else QuizResult.Error("Server error: ${response.code()}")
        } catch (e: Exception) {
            QuizResult.Error(e.localizedMessage ?: "Network error")
        }
    }

    suspend fun generateDialogue(
        poemText: String,
        language: String,
        numCharacters: Int
    ): DialogueResult {
        return try {
            val response = apiService.generateDialogue(
                DialogueRequest(poemText, language, numCharacters)
            )
            if (response.isSuccessful && response.body() != null)
                DialogueResult.Success(response.body()!!)
            else DialogueResult.Error("Server error: ${response.code()}")
        } catch (e: Exception) {
            DialogueResult.Error(e.localizedMessage ?: "Network error")
        }
    }

    suspend fun generatePoem(
        poetName: String,
        topic: String,
        language: String,
        length: String
    ): GenerateResult {
        return try {
            val response = apiService.generatePoem(
                GenerateRequest(poetName, topic, language, length)
            )
            if (response.isSuccessful && response.body() != null)
                GenerateResult.Success(response.body()!!)
            else GenerateResult.Error("Server error: ${response.code()}")
        } catch (e: Exception) {
            GenerateResult.Error(e.localizedMessage ?: "Network error")
        }
    }

    // ── Group C ───────────────────────────────────────────────────────────

    suspend fun scoreDifficulty(
        poemText: String,
        language: String
    ): DifficultyResult {
        return try {
            val response = apiService.scoreDifficulty(
                ExplainRequest(poemText, language, "full")
            )
            if (response.isSuccessful && response.body() != null)
                DifficultyResult.Success(response.body()!!)
            else DifficultyResult.Error("Server error: ${response.code()}")
        } catch (e: Exception) {
            DifficultyResult.Error(e.localizedMessage ?: "Network error")
        }
    }

    suspend fun transliterate(
        text: String,
        direction: String,
        style: String
    ): TransliterateResult {
        return try {
            val response = apiService.transliterate(
                TransliterateRequest(text, direction, style)
            )
            if (response.isSuccessful && response.body() != null)
                TransliterateResult.Success(response.body()!!)
            else TransliterateResult.Error("Server error: ${response.code()}")
        } catch (e: Exception) {
            TransliterateResult.Error(e.localizedMessage ?: "Network error")
        }
    }

    // ── Private helper ────────────────────────────────────────────────────

    private suspend fun safeCall(
        call: suspend () -> retrofit2.Response<ExplainResponse>
    ): ExplainResult {
        return try {
            val response = call()
            if (response.isSuccessful && response.body() != null)
                ExplainResult.Success(response.body()!!)
            else ExplainResult.Error("Server error: ${response.code()}")
        } catch (e: Exception) {
            ExplainResult.Error(e.localizedMessage ?: "Network error")
        }
    }
}

// ── Result sealed classes ─────────────────────────────────────────────────

sealed class ExplainResult {
    data class Success(val data: ExplainResponse) : ExplainResult()
    data class Error(val message: String) : ExplainResult()
}

sealed class StoryResult {
    data class Success(val data: StoryResponse) : StoryResult()
    data class Error(val message: String) : StoryResult()
}

sealed class PoetResult {
    data class Success(val data: PoetResponse) : PoetResult()
    data class Error(val message: String) : PoetResult()
}

sealed class QuizResult {
    data class Success(val data: QuizResponse) : QuizResult()
    data class Error(val message: String) : QuizResult()
}

sealed class DialogueResult {
    data class Success(val data: DialogueResponse) : DialogueResult()
    data class Error(val message: String) : DialogueResult()
}

sealed class GenerateResult {
    data class Success(val data: GenerateResponse) : GenerateResult()
    data class Error(val message: String) : GenerateResult()
}

sealed class DifficultyResult {
    data class Success(val data: DifficultyResponse) : DifficultyResult()
    data class Error(val message: String) : DifficultyResult()
}

sealed class TransliterateResult {
    data class Success(val data: TransliterateResponse) : TransliterateResult()
    data class Error(val message: String) : TransliterateResult()
}