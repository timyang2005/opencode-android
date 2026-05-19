package com.opencode.app.presentation.screens.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.opencode.app.domain.model.Message
import com.opencode.app.domain.model.MessagePart
import com.opencode.app.domain.model.MessageRole
import com.opencode.app.domain.model.SessionStatus
import com.opencode.app.domain.repository.ChatRepository
import com.opencode.app.domain.repository.SessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.UUID
import javax.inject.Inject

data class ChatUiState(
    val messages: List<Message> = emptyList(),
    val inputText: String = "",
    val isLoading: Boolean = false,
    val isWaitingForInput: Boolean = false,
    val currentSession: SessionInfo? = null,
    val availableSessions: List<SessionInfo> = emptyList()
)

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    init {
        loadAvailableSessions()
    }

    private fun loadAvailableSessions() {
        viewModelScope.launch {
            try {
                val sessions = sessionRepository.getSessions().first()
                val sessionInfos = sessions.map { session ->
                    SessionInfo(
                        id = session.id,
                        title = session.title,
                        status = session.status
                    )
                }
                _uiState.value = _uiState.value.copy(availableSessions = sessionInfos)
            } catch (_: Exception) {
                // 加载失败时保持空列表
            }
        }
    }

    fun loadSession(id: String) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                val session = sessionRepository.getSession(id).first()
                if (session != null) {
                    val sessionInfo = SessionInfo(
                        id = session.id,
                        title = session.title,
                        status = session.status
                    )
                    _uiState.value = _uiState.value.copy(
                        messages = session.messages,
                        currentSession = sessionInfo,
                        isLoading = false,
                        isWaitingForInput = session.status == SessionStatus.WAITING
                    )
                } else {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
            } catch (_: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    fun selectSession(session: SessionInfo) {
        loadSession(session.id)
    }

    fun sendMessage() {
        val currentInput = _uiState.value.inputText
        val currentSession = _uiState.value.currentSession ?: return
        if (currentInput.isBlank()) return

        viewModelScope.launch {
            val userMessage = Message(
                id = UUID.randomUUID().toString(),
                sessionId = currentSession.id,
                role = MessageRole.USER,
                parts = listOf(MessagePart.Text(text = currentInput)),
                createdAt = Instant.now()
            )

            _uiState.value = _uiState.value.copy(
                messages = _uiState.value.messages + userMessage,
                inputText = "",
                isLoading = true,
                isWaitingForInput = false
            )

            try {
                val responseMessage = chatRepository.sendMessage(
                    sessionId = currentSession.id,
                    text = currentInput
                ).first()

                _uiState.value = _uiState.value.copy(
                    messages = _uiState.value.messages + responseMessage,
                    isLoading = false
                )
            } catch (_: Exception) {
                val errorMessage = Message(
                    id = UUID.randomUUID().toString(),
                    sessionId = currentSession.id,
                    role = MessageRole.SYSTEM,
                    parts = listOf(MessagePart.Text(text = "发送失败，请检查网络连接后重试。")),
                    createdAt = Instant.now()
                )
                _uiState.value = _uiState.value.copy(
                    messages = _uiState.value.messages + errorMessage,
                    isLoading = false
                )
            }
        }
    }

    fun createSession() {
        viewModelScope.launch {
            try {
                val session = sessionRepository.createSession("新会话").first()
                val sessionInfo = SessionInfo(
                    id = session.id,
                    title = session.title,
                    status = session.status
                )
                _uiState.value = _uiState.value.copy(
                    currentSession = sessionInfo,
                    messages = emptyList(),
                    availableSessions = _uiState.value.availableSessions + sessionInfo
                )
            } catch (_: Exception) {
                // 创建失败
            }
        }
    }

    fun onInputTextChange(text: String) {
        _uiState.value = _uiState.value.copy(inputText = text)
    }

    fun cancelOperation() {
        _uiState.value = _uiState.value.copy(
            isLoading = false,
            isWaitingForInput = false
        )
    }
}
