package com.kavyakanaja.app.data.db

import androidx.room.*
import com.kavyakanaja.app.model.FavoritePoem
import kotlinx.coroutines.flow.Flow

/**
 * DAO (Data Access Object) for favorite poems.
 * Uses Flow to automatically emit updates when DB changes.
 */
@Dao
interface FavoritePoemDao {

    @Query("SELECT * FROM favorite_poems ORDER BY savedAt DESC")
    fun getAllFavorites(): Flow<List<FavoritePoem>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_poems WHERE poemId = :id)")
    fun isFavorite(id: Int): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(poem: FavoritePoem)

    @Query("DELETE FROM favorite_poems WHERE poemId = :id")
    suspend fun removeFavorite(id: Int)
}