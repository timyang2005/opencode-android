package com.opencode.app.domain.model

import java.time.Instant

/**
 * OpenCode会话模型
 */
data class Session(
    val id: String,
    val title: String,
    val parentId: String? = null,
    val createdAt: Instant,
    val updatedAt: Instant,
    val status: SessionStatus = SessionStatus.IDLE,
    val isShared: Boolean = false,
    val shareId: String? = null,
    val cost: Double = 0.0,
    val tokens: Int = 0,
    val directory: String? = null,
    val messages: List<Message> = emptyList()
)

enum class SessionStatus {
    IDLE, RUNNING, WAITING, ERROR
}

data class Message(
    val id: String,
    val sessionId: String,
    val role: MessageRole,
    val parts: List<MessagePart>,
    val createdAt: Instant,
    val cost: Double = 0.0,
    val tokens: Int = 0,
    val model: ModelInfo? = null
)

enum class MessageRole {
    USER, ASSISTANT, SYSTEM
}

sealed class MessagePart {
    data class Text(val text: String) : MessagePart()
    data class Code(val code: String, val language: String? = null) : MessagePart()
    data class ToolCall(val toolName: String, val params: String, val result: String? = null) : MessagePart()
    data class File(val path: String, val content: String? = null) : MessagePart()
    data class Image(val url: String, val description: String? = null) : MessagePart()
}

data class ModelInfo(
    val providerId: String,
    val modelId: String,
    val displayName: String
)

data class ServerConfig(
    val host: String = "localhost",
    val port: Int = 4096,
    val useHttps: Boolean = false,
    val username: String = "opencode",
    val password: String? = null
) {
    val baseUrl: String
        get() = "${if (useHttps) "https" else "http"}://$host:$port"
}

data class ServerHealth(val healthy: Boolean, val version: String)

data class FileInfo(
    val path: String,
    val name: String,
    val isDirectory: Boolean,
    val size: Long = 0,
    val modifiedAt: Instant? = null,
    val content: String? = null
)