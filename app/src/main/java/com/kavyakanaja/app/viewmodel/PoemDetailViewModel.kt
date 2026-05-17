package com.kavyakanaja.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kavyakanaja.app.data.api.RetrofitClient
import com.kavyakanaja.app.data.db.AppDatabase
import com.kavyakanaja.app.data.repository.*
import com.kavyakanaja.app.model.*
import com.kavyakanaja.app.utils.Constants
import com.kavyakanaja.app.utils.TTSManager
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/** All possible UI states for any AI operation */
sealed class AiUiState {
    object Idle    : AiUiState()
    object Loading : AiUiState()
    data class ExplainSuccess(val data: ExplainResponse)       : AiUiState()
    data class StorySuccess(val data: StoryResponse)           : AiUiState()
    data class DialogueSuccess(val data: DialogueResponse)     : AiUiState()
    data class DifficultySuccess(val data: DifficultyResponse) : AiUiState()
    data class TranslitSuccess(val data: TransliterateResponse): AiUiState()
    data class WordSuccess(val data: ExplainResponse)          : AiUiState()
    data class Error(val message: String)                      : AiUiState()
}

class PoemDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val db             = AppDatabase.getInstance(application)
    private val poemRepo       = PoemRepository(RetrofitClient.apiService, db.favoritePoemDao(), db.viewedPoemDao())
    private val explainRepo    = ExplainRepository(RetrofitClient.apiService)
    val ttsManager             = TTSManager(application)

    // ── Poem state ────────────────────────────────────────────────────────
    private val _poem = MutableStateFlow<PoemData?>(null)
    val poem: StateFlow<PoemData?> = _poem.asStateFlow()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()

    private val _relatedPoems = MutableStateFlow<List<PoemData>>(emptyList())
    val relatedPoems: StateFlow<List<PoemData>> = _relatedPoems.asStateFlow()

    // ── AI state — single StateFlow handles all AI results ────────────────
    private val _aiState = MutableStateFlow<AiUiState>(AiUiState.Idle)
    val aiState: StateFlow<AiUiState> = _aiState.asStateFlow()

    // ── Word meaning state ────────────────────────────────────────────────
    private val _wordState = MutableStateFlow<AiUiState>(AiUiState.Idle)
    val wordState: StateFlow<AiUiState> = _wordState.asStateFlow()

    // ── TTS state ─────────────────────────────────────────────────────────
    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

    private val _ttsReady = MutableStateFlow(false)
    val ttsReady: StateFlow<Boolean> = _ttsReady.asStateFlow()

    private val _language = MutableStateFlow(Constants.LANG_ENGLISH)
    val language: StateFlow<String> = _language.asStateFlow()

    init {
        ttsManager.onInitialized    = { ok -> _ttsReady.value = ok }
        ttsManager.onSpeakingStarted  = { _isSpeaking.value = true }
        ttsManager.onSpeakingFinished = { _isSpeaking.value = false }
    }

    // ── Load poem ─────────────────────────────────────────────────────────

    fun loadPoem(poemId: Int, language: String) {
        _language.value = language
        viewModelScope.launch {
            when (val r = poemRepo.getPoemById(poemId)) {
                is PoemResult.Success -> {
                    _poem.value = r.data
                    poemRepo.markAsViewed(r.data)
                    loadRelated(poemId)
                }
                is PoemResult.Error -> _aiState.value = AiUiState.Error(r.message)
            }
            poemRepo.isFavorite(poemId).collect { _isFavorite.value = it }
        }
    }

    private fun loadRelated(poemId: Int) {
        viewModelScope.launch {
            when (val r = poemRepo.getRelatedPoems(poemId)) {
                is PoemResult.Success -> _relatedPoems.value = r.data
                is PoemResult.Error   -> { /* silent */ }
            }
        }
    }

    // ── Group A — explain modes ───────────────────────────────────────────

    fun explainPoem()   = callExplain(Constants.MODE_FULL)
    fun detectEmotion() = callExplain(Constants.MODE_EMOTION)
    fun extractLesson() = callExplain(Constants.MODE_LESSON)
    fun simplifyPoem()  = callExplain(Constants.MODE_SIMPLIFY)
    fun modernizePoem() = callExplain(Constants.MODE_MODERNIZE)
    fun visualizePoem() = callExplain(Constants.MODE_VISUALIZE)
    fun getBackground() = callExplain(Constants.MODE_BACKGROUND)

    private fun callExplain(mode: String) {
        val poem = _poem.value ?: return
        _aiState.value = AiUiState.Loading
        viewModelScope.launch {
            _aiState.value = when (val r = explainRepo.getExplanation(poem.text, _language.value, mode)) {
                is ExplainResult.Success -> AiUiState.ExplainSuccess(r.data)
                is ExplainResult.Error   -> AiUiState.Error(r.message)
            }
        }
    }

    // ── Word meaning ──────────────────────────────────────────────────────

    fun explainWord(word: String) {
        _wordState.value = AiUiState.Loading
        viewModelScope.launch {
            _wordState.value = when (val r = explainRepo.getExplanation(word, _language.value, Constants.MODE_WORD)) {
                is ExplainResult.Success -> AiUiState.WordSuccess(r.data)
                is ExplainResult.Error   -> AiUiState.Error(r.message)
            }
        }
    }

    fun clearWordState() { _wordState.value = AiUiState.Idle }

    // ── Group B ───────────────────────────────────────────────────────────

    fun generateStory(style: String = Constants.STYLE_SIMPLE) {
        val poem = _poem.value ?: return
        _aiState.value = AiUiState.Loading
        viewModelScope.launch {
            _aiState.value = when (val r = explainRepo.generateStory(poem.text, _language.value, style)) {
                is StoryResult.Success -> AiUiState.StorySuccess(r.data)
                is StoryResult.Error   -> AiUiState.Error(r.message)
            }
        }
    }

    fun generateDialogue(numCharacters: Int = 2) {
        val poem = _poem.value ?: return
        _aiState.value = AiUiState.Loading
        viewModelScope.launch {
            _aiState.value = when (val r = explainRepo.generateDialogue(poem.text, _language.value, numCharacters)) {
                is DialogueResult.Success -> AiUiState.DialogueSuccess(r.data)
                is DialogueResult.Error   -> AiUiState.Error(r.message)
            }
        }
    }

    // ── Group C ───────────────────────────────────────────────────────────

    fun scoreDifficulty() {
        val poem = _poem.value ?: return
        _aiState.value = AiUiState.Loading
        viewModelScope.launch {
            _aiState.value = when (val r = explainRepo.scoreDifficulty(poem.text, _language.value)) {
                is DifficultyResult.Success -> AiUiState.DifficultySuccess(r.data)
                is DifficultyResult.Error   -> AiUiState.Error(r.message)
            }
        }
    }

    fun transliterate() {
        val poem = _poem.value ?: return
        _aiState.value = AiUiState.Loading
        viewModelScope.launch {
            _aiState.value = when (val r = explainRepo.transliterate(poem.text, Constants.DIR_KN_TO_ROMAN, "readable")) {
                is TransliterateResult.Success -> AiUiState.TranslitSuccess(r.data)
                is TransliterateResult.Error   -> AiUiState.Error(r.message)
            }
        }
    }

    // ── Favorites ─────────────────────────────────────────────────────────

    fun toggleFavorite() {
        val poem = _poem.value ?: return
        viewModelScope.launch {
            if (_isFavorite.value) poemRepo.removeFavorite(poem.id)
            else poemRepo.addFavorite(poem)
        }
    }

    // ── TTS ───────────────────────────────────────────────────────────────

    fun playPoemAudio() {
        val poem = _poem.value ?: return
        if (ttsManager.isSpeaking()) ttsManager.stop()
        else ttsManager.speak(poem.text, _language.value)
    }

    fun speakText(text: String) {
        if (ttsManager.isSpeaking()) ttsManager.stop()
        else ttsManager.speak(text, _language.value)
    }

    fun resetAiState() { _aiState.value = AiUiState.Idle }

    override fun onCleared() {
        super.onCleared()
        ttsManager.shutdown()
    }
}