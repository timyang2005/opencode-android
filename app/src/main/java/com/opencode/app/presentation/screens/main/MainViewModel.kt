package com.opencode.app.presentation.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.opencode.app.domain.model.ServerHealth
import com.opencode.app.domain.repository.SessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MainUiState(
    val isConnected: Boolean = true,
    val serverVersion: String = "1.0.0",
    val pendingQuestions: Int = 0
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        checkServerHealth()
    }

    private fun checkServerHealth() {
        viewModelScope.launch {
            try {
                // Mock: 直接标记为已连接
                _uiState.value = _uiState.value.copy(
                    isConnected = true,
                    serverVersion = "1.0.0"
                )
            } catch (_: Exception) {
                _uiState.value = _uiState.value.copy(isConnected = false)
            }
        }
    }

    fun refreshConnectionStatus() {
        checkServerHealth()
    }
}
