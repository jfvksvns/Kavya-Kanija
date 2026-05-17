package com.kavyakanaja.app.data.repository

import com.kavyakanaja.app.data.api.ApiService
import com.kavyakanaja.app.data.db.FavoritePoemDao
import com.kavyakanaja.app.data.db.ViewedPoemDao
import com.kavyakanaja.app.model.*
import kotlinx.coroutines.flow.Flow

/**
 * Repository for poem data.
 * UPDATED: Poems now come from the backend API instead of local SamplePoems.
 */
class PoemRepository(
    private val apiService: ApiService,
    private val favoriteDao: FavoritePoemDao,
    private val viewedDao: ViewedPoemDao
) {
    // ── Remote poem data ──────────────────────────────────────────────────

    suspend fun getAllPoems(
        search: String = "",
        category: String = "",
        difficulty: String = "",
        poet: String = "",
        page: Int = 1
    ): PoemResult<PoemsListResponse> {
        return safeApiCall { apiService.getPoems(search, category, difficulty, poet, page) }
    }

    suspend fun getDailyPoem(): PoemResult<PoemData> {
        return safeApiCall { apiService.getDailyPoem() }
    }

    suspend fun getPoemById(id: Int): PoemResult<PoemData> {
        return safeApiCall { apiService.getPoemById(id) }
    }

    suspend fun getRelatedPoems(id: Int): PoemResult<List<PoemData>> {
        return safeApiCall { apiService.getRelatedPoems(id) }
    }

    suspend fun getPoemsMeta(): PoemResult<PoemsMetaResponse> {
        return safeApiCall { apiService.getPoemsMeta() }
    }

    // ── Favorites (Room DB — unchanged) ───────────────────────────────────

    fun getAllFavorites(): Flow<List<FavoritePoem>> = favoriteDao.getAllFavorites()

    fun isFavorite(poemId: Int): Flow<Boolean> = favoriteDao.isFavorite(poemId)

    suspend fun addFavorite(poem: PoemData) {
        favoriteDao.addFavorite(
            FavoritePoem(
                poemId = poem.id,
                titleEn = poem.titleEn,
                titleKn = poem.titleKn,
                poet = poem.poet,
                text = poem.text
            )
        )
    }

    suspend fun removeFavorite(poemId: Int) = favoriteDao.removeFavorite(poemId)

    // ── History (Room DB — unchanged) ─────────────────────────────────────

    fun getRecentlyViewed(): Flow<List<ViewedPoem>> = viewedDao.getRecentlyViewed()

    suspend fun markAsViewed(poem: PoemData) {
        viewedDao.markAsViewed(
            ViewedPoem(
                poemId = poem.id,
                titleEn = poem.titleEn,
                titleKn = poem.titleKn,
                poet = poem.poet,
                text = poem.text
            )
        )
    }

    // ── Helper ────────────────────────────────────────────────────────────

    private suspend fun <T> safeApiCall(
        call: suspend () -> retrofit2.Response<T>
    ): PoemResult<T> {
        return try {
            val response = call()
            if (response.isSuccessful && response.body() != null) {
                PoemResult.Success(response.body()!!)
            } else {
                PoemResult.Error("Server error: ${response.code()}")
            }
        } catch (e: Exception) {
            PoemResult.Error(e.localizedMessage ?: "Network error occurred")
        }
    }
}

sealed class PoemResult<T> {
    data class Success<T>(val data: T) : PoemResult<T>()
    data class Error<T>(val message: String) : PoemResult<T>()
}