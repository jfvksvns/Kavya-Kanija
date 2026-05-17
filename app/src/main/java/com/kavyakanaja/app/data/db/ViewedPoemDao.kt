package com.kavyakanaja.app.data.db

import androidx.room.*
import com.kavyakanaja.app.model.ViewedPoem
import kotlinx.coroutines.flow.Flow

/**
 * DAO for tracking poem view history.
 */
@Dao
interface ViewedPoemDao {

    @Query("SELECT * FROM viewed_poems ORDER BY viewedAt DESC LIMIT 20")
    fun getRecentlyViewed(): Flow<List<ViewedPoem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun markAsViewed(poem: ViewedPoem)

    @Query("DELETE FROM viewed_poems WHERE viewedAt < :cutoff")
    suspend fun clearOldHistory(cutoff: Long)
}