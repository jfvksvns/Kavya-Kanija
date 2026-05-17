package com.kavyakanaja.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kavyakanaja.app.data.api.RetrofitClient
import com.kavyakanaja.app.data.db.AppDatabase
import com.kavyakanaja.app.data.repository.PoemRepository
import com.kavyakanaja.app.data.repository.PoemResult
import com.kavyakanaja.app.model.PoemData
import com.kavyakanaja.app.model.PoemsMetaResponse
import com.kavyakanaja.app.utils.LanguageManager
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    private val repository = PoemRepository(
        RetrofitClient.apiService,
        db.favoritePoemDao(),
        db.viewedPoemDao()
    )
    val languageManager = LanguageManager(application)

    // ── UI State ──────────────────────────────────────────────────────────

    private val _poems = MutableStateFlow<List<PoemData>>(emptyList())
    val poems: StateFlow<List<PoemData>> = _poems.asStateFlow()

    private val _dailyPoem = MutableStateFlow<PoemData?>(null)
    val dailyPoem: StateFlow<PoemData?> = _dailyPoem.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _selectedDifficulty = MutableStateFlow("")
    val selectedDifficulty: StateFlow<String> = _selectedDifficulty.asStateFlow()

    private val _meta = MutableStateFlow<PoemsMetaResponse?>(null)
    val meta: StateFlow<PoemsMetaResponse?> = _meta.asStateFlow()

    val selectedLanguage: StateFlow<String> = languageManager.selectedLanguage
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "en")

    // ── Init ──────────────────────────────────────────────────────────────

    init {
        loadPoems()
        loadDailyPoem()
        loadMeta()
    }

    // ── Data loading ──────────────────────────────────────────────────────

    fun loadPoems() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            when (val result = repository.getAllPoems(
                search = _searchQuery.value,
                category = _selectedCategory.value,
                difficulty = _selectedDifficulty.value
            )) {
                is PoemResult.Success -> _poems.value = result.data.poems
                is PoemResult.Error   -> _error.value = result.message
            }
            _isLoading.value = false
        }
    }

    private fun loadDailyPoem() {
        viewModelScope.launch {
            when (val result = repository.getDailyPoem()) {
                is PoemResult.Success -> _dailyPoem.value = result.data
                is PoemResult.Error   -> { /* silent fail for daily poem */ }
            }
        }
    }

    private fun loadMeta() {
        viewModelScope.launch {
            when (val result = repository.getPoemsMeta()) {
                is PoemResult.Success -> _meta.value = result.data
                is PoemResult.Error   -> { /* silent fail */ }
            }
        }
    }

    // ── Actions ───────────────────────────────────────────────────────────

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        loadPoems()
    }

    fun onCategorySelected(category: String) {
        _selectedCategory.value = if (_selectedCategory.value == category) "" else category
        loadPoems()
    }

    fun onDifficultySelected(difficulty: String) {
        _selectedDifficulty.value = if (_selectedDifficulty.value == difficulty) "" else difficulty
        loadPoems()
    }

    fun clearFilters() {
        _searchQuery.value = ""
        _selectedCategory.value = ""
        _selectedDifficulty.value = ""
        loadPoems()
    }

    fun toggleLanguage() {
        viewModelScope.launch {
            val next = if (selectedLanguage.value == "en") "kn" else "en"
            languageManager.setLanguage(next)
        }
    }
}