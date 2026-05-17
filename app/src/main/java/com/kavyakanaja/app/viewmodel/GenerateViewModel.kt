package com.kavyakanaja.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kavyakanaja.app.data.api.RetrofitClient
import com.kavyakanaja.app.data.repository.ExplainRepository
import com.kavyakanaja.app.data.repository.GenerateResult
import com.kavyakanaja.app.model.GenerateResponse
import com.kavyakanaja.app.utils.Constants
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class GenerateUiState {
    object Idle    : GenerateUiState()
    object Loading : GenerateUiState()
    data class Success(val data: GenerateResponse) : GenerateUiState()
    data class Error(val message: String) : GenerateUiState()
}

class GenerateViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ExplainRepository(RetrofitClient.apiService)

    private val _state = MutableStateFlow<GenerateUiState>(GenerateUiState.Idle)
    val state: StateFlow<GenerateUiState> = _state.asStateFlow()

    // Form inputs
    private val _poetName  = MutableStateFlow("")
    val poetName: StateFlow<String> = _poetName.asStateFlow()

    private val _topic = MutableStateFlow("")
    val topic: StateFlow<String> = _topic.asStateFlow()

    private val _length = MutableStateFlow(Constants.LENGTH_SHORT)
    val length: StateFlow<String> = _length.asStateFlow()

    fun onPoetNameChange(v: String)  { _poetName.value = v }
    fun onTopicChange(v: String)     { _topic.value = v }
    fun onLengthChange(v: String)    { _length.value = v }

    fun generate(language: String) {
        if (_poetName.value.isBlank() || _topic.value.isBlank()) return
        _state.value = GenerateUiState.Loading
        viewModelScope.launch {
            _state.value = when (val r = repository.generatePoem(
                _poetName.value.trim(),
                _topic.value.trim(),
                language,
                _length.value
            )) {
                is GenerateResult.Success -> GenerateUiState.Success(r.data)
                is GenerateResult.Error   -> GenerateUiState.Error(r.message)
            }
        }
    }

    fun reset() { _state.value = GenerateUiState.Idle }
}