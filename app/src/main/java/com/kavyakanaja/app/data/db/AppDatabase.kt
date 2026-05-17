package com.kavyakanaja.app.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kavyakanaja.app.model.FavoritePoem
import com.kavyakanaja.app.model.ViewedPoem
import com.kavyakanaja.app.utils.Constants

/**
 * Room database definition.
 * Acts as the single source of truth for all local data.
 */
@Database(
    entities = [FavoritePoem::class, ViewedPoem::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun favoritePoemDao(): FavoritePoemDao
    abstract fun viewedPoemDao(): ViewedPoemDao

    companion object {
        // Volatile ensures visibility across threads
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Returns singleton database instance.
         * Creates the database only once using double-check locking.
         */
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    Constants.DB_NAME
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}