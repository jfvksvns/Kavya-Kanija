package com.kavyakanaja.app.data.api

import com.kavyakanaja.app.model.*
import retrofit2.Response
import retrofit2.http.*

/**
 * Complete Retrofit API interface for Kavya-Kanaja backend v3.0.0
 *
 * Endpoints:
 *  POST /explain        — poem/word explanation (all modes)
 *  POST /story          — poem to story
 *  POST /poet           — poet profile
 *  POST /quiz           — quiz generator
 *  POST /dialogue       — poem to dialogue
 *  POST /generate       — write like poet
 *  POST /difficulty     — difficulty scorer
 *  POST /transliterate  — script conversion
 *  GET  /poems          — paginated poem list
 *  GET  /poems/daily    — today's poem
 *  GET  /poems/meta     — categories + poets
 *  GET  /poems/related/{id} — related poems
 *  GET  /poems/{id}     — single poem
 */
interface ApiService {

    // ── Original ──────────────────────────────────────────────────────────
    @POST("explain")
    suspend fun explainPoem(
        @Body request: ExplainRequest
    ): Response<ExplainResponse>

    // ── Group B ───────────────────────────────────────────────────────────

    @POST("story")
    suspend fun generateStory(
        @Body request: StoryRequest
    ): Response<StoryResponse>

    @POST("poet")
    suspend fun getPoetProfile(
        @Body request: PoetRequest
    ): Response<PoetResponse>

    @POST("quiz")
    suspend fun generateQuiz(
        @Body request: QuizRequest
    ): Response<QuizResponse>

    @POST("dialogue")
    suspend fun generateDialogue(
        @Body request: DialogueRequest
    ): Response<DialogueResponse>

    @POST("generate")
    suspend fun generatePoem(
        @Body request: GenerateRequest
    ): Response<GenerateResponse>

    // ── Group C ───────────────────────────────────────────────────────────

    @POST("difficulty")
    suspend fun scoreDifficulty(
        @Body request: ExplainRequest
    ): Response<DifficultyResponse>

    @POST("transliterate")
    suspend fun transliterate(
        @Body request: TransliterateRequest
    ): Response<TransliterateResponse>

    // ── Poems endpoints ───────────────────────────────────────────────────

    @GET("poems")
    suspend fun getPoems(
        @Query("search")     search: String = "",
        @Query("category")   category: String = "",
        @Query("difficulty") difficulty: String = "",
        @Query("poet")       poet: String = "",
        @Query("page")       page: Int = 1,
        @Query("page_size")  pageSize: Int = 20
    ): Response<PoemsListResponse>

    @GET("poems/daily")
    suspend fun getDailyPoem(): Response<PoemData>

    @GET("poems/meta")
    suspend fun getPoemsMeta(): Response<PoemsMetaResponse>

    @GET("poems/related/{id}")
    suspend fun getRelatedPoems(
        @Path("id") poemId: Int
    ): Response<List<PoemData>>

    @GET("poems/{id}")
    suspend fun getPoemById(
        @Path("id") poemId: Int
    ): Response<PoemData>
}