package com.kavyakanaja.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kavyakanaja.app.data.api.RetrofitClient
import com.kavyakanaja.app.data.repository.ExplainRepository
import com.kavyakanaja.app.data.repository.PoetResult
import com.kavyakanaja.app.model.PoetResponse
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class PoetUiState {
    object Idle    : PoetUiState()
    object Loading : PoetUiState()
    data class Success(val data: PoetResponse) : PoetUiState()
    data class Error(val message: String) : PoetUiState()
}

class PoetViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ExplainRepository(RetrofitClient.apiService)

    private val _state = MutableStateFlow<PoetUiState>(PoetUiState.Idle)
    val state: StateFlow<PoetUiState> = _state.asStateFlow()

    private val _poetName = MutableStateFlow("")
    val poetName: StateFlow<String> = _poetName.asStateFlow()

    fun loadPoetProfile(name: String, language: String) {
        _poetName.value = name
        _state.value = PoetUiState.Loading
        viewModelScope.launch {
            _state.value = when (val r = repository.getPoetProfile(name, language)) {
                is PoetResult.Success -> PoetUiState.Success(r.data)
                is PoetResult.Error   -> PoetUiState.Error(r.message)
            }
        }
    }
}