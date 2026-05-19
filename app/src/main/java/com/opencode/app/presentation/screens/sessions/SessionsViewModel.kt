package com.opencode.app.presentation.screens.sessions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.opencode.app.domain.model.Session
import com.opencode.app.domain.repository.SessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 会话列表界面 ViewModel
 */
data class SessionsUiState(
    val sessions: List<Session> = emptyList(),
    val isLoading: Boolean = false
)

@HiltViewModel
class SessionsViewModel @Inject constructor(
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SessionsUiState())
    val uiState: StateFlow<SessionsUiState> = _uiState.asStateFlow()

    init {
        loadSessions()
    }

    fun loadSessions() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val sessions = sessionRepository.getSessions().first()
                _uiState.value = _uiState.value.copy(
                    sessions = sessions,
                    isLoading = false
                )
            } catch (_: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    fun createSession(title: String) {
        viewModelScope.launch {
            try {
                val newSession = sessionRepository.createSession(title.ifBlank { "新会话" }).first()
                _uiState.value = _uiState.value.copy(
                    sessions = listOf(newSession) + _uiState.value.sessions
                )
            } catch (_: Exception) {
                // 创建失败
            }
        }
    }

    fun deleteSession(id: String) {
        viewModelScope.launch {
            try {
                val success = sessionRepository.deleteSession(id).first()
                if (success) {
                    _uiState.value = _uiState.value.copy(
                        sessions = _uiState.value.sessions.filter { it.id != id }
                    )
                }
            } catch (_: Exception) {
                // 删除失败
            }
        }
    }

    fun startRenameSession(session: Session) {
        viewModelScope.launch {
            try {
                val success = sessionRepository.updateSession(session.id, session.title).first()
                if (success) {
                    _uiState.value = _uiState.value.copy(
                        sessions = _uiState.value.sessions.map {
                            if (it.id == session.id) it.copy(title = session.title) else it
                        }
                    )
                }
            } catch (_: Exception) {
                // 重命名失败
            }
        }
    }

    fun shareSession(id: String) {
        // 分享功能暂不支持，仅作为 UI 占位
    }

    fun forkSession(id: String) {
        // Fork 功能暂不支持，仅作为 UI 占位
    }
}
