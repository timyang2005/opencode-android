package com.opencode.app.domain.repository

import com.opencode.app.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun getMessages(sessionId: String): Flow<List<Message>>
    fun sendMessage(sessionId: String, text: String): Flow<Message>
}