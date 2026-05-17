package com.kavyakanaja.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kavyakanaja.app.data.api.RetrofitClient
import com.kavyakanaja.app.data.db.AppDatabase
import com.kavyakanaja.app.data.repository.PoemRepository
import com.kavyakanaja.app.model.FavoritePoem
import com.kavyakanaja.app.model.ViewedPoem
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel for Favorites and History screens.
 */
class FavoritesViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    private val repository = PoemRepository(RetrofitClient.apiService, db.favoritePoemDao(), db.viewedPoemDao())

    /** All saved favorite poems */
    val favorites: StateFlow<List<FavoritePoem>> = repository.getAllFavorites()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /** Recently viewed poems */
    val recentlyViewed: StateFlow<List<ViewedPoem>> = repository.getRecentlyViewed()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun removeFavorite(poemId: Int) {
        viewModelScope.launch { repository.removeFavorite(poemId) }
    }
}
