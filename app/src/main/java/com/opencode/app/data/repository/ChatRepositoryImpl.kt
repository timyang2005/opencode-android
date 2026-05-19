package com.opencode.app.data.repository

import com.opencode.app.domain.model.Message
import com.opencode.app.domain.model.MessagePart
import com.opencode.app.domain.model.MessageRole
import com.opencode.app.domain.model.ModelInfo
import com.opencode.app.domain.repository.ChatRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.Instant
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepositoryImpl @Inject constructor() : ChatRepository {

    private val mockMessages = mutableMapOf<String, MutableList<Message>>()

    init {
        // 预置 session-001 的消息
        mockMessages["session-001"] = mutableListOf(
            Message(
                id = "msg-001-1",
                sessionId = "session-001",
                role = MessageRole.USER,
                parts = listOf(
                    MessagePart.Text("帮我创建一个新的 Android 项目，使用 Kotlin + Jetpack Compose + Hilt")
                ),
                createdAt = Instant.now().minusSeconds(3600 * 2)
            ),
            Message(
                id = "msg-001-2",
                sessionId = "session-001",
                role = MessageRole.ASSISTANT,
                parts = listOf(
                    MessagePart.Text("好的，我来帮你创建一个使用 Kotlin + Jetpack Compose + Hilt 的 Android 项目。"),
                    MessagePart.Code(
                        code = """// build.gradle.kts
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id("org.jetbrains.kotlin.kapt")
}""",
                        language = "kotlin"
                    )
                ),
                createdAt = Instant.now().minusSeconds(3600 * 2 + 30),
                model = ModelInfo(
                    providerId = "openai",
                    modelId = "gpt-4",
                    displayName = "GPT-4"
                )
            )
        )
    }

    override fun getMessages(sessionId: String): Flow<List<Message>> = flow {
        emit(mockMessages[sessionId]?.toList() ?: emptyList())
    }

    override fun sendMessage(sessionId: String, text: String): Flow<Message> = flow {
        // 先添加用户消息
        val userMessage = Message(
            id = "msg-${UUID.randomUUID()}",
            sessionId = sessionId,
            role = MessageRole.USER,
            parts = listOf(MessagePart.Text(text)),
            createdAt = Instant.now()
        )
        mockMessages.getOrPut(sessionId) { mutableListOf() }.add(userMessage)

        // 模拟 AI 处理延迟
        delay(1000)

        // 生成模拟 AI 回复
        val assistantMessage = generateMockReply(sessionId, text)
        mockMessages[sessionId]?.add(assistantMessage)

        emit(assistantMessage)
    }

    private fun generateMockReply(sessionId: String, userText: String): Message {
        val replyParts = mutableListOf<MessagePart>()

        when {
            userText.contains("代码", ignoreCase = true) || userText.contains("code", ignoreCase = true) -> {
                replyParts.add(
                    MessagePart.Text("好的，这是你需要的代码示例：")
                )
                replyParts.add(
                    MessagePart.Code(
                        code = """fun main() {
    println("Hello, OpenCode!")
    
    val items = listOf("Kotlin", "Compose", "Hilt")
    items.forEach { item ->
        println("- \$item")
    }
}""",
                        language = "kotlin"
                    )
                )
                replyParts.add(
                    MessagePart.Text("这段代码展示了 Kotlin 的基本语法，包括字符串模板和集合操作。有什么问题可以继续问我。")
                )
            }

            userText.contains("解释", ignoreCase = true) || userText.contains("explain", ignoreCase = true) -> {
                replyParts.add(
                    MessagePart.Text(
                        "这是一个很好的问题。让我来详细解释一下：\n\n" +
                        "1. **第一点**：核心概念是模块化和关注点分离。\n" +
                        "2. **第二点**：MVVM 架构将 UI 逻辑与业务逻辑分离，使代码更易于测试和维护。\n" +
                        "3. **第三点**：Hilt 提供了依赖注入的能力，减少了手动管理依赖的复杂度。\n\n" +
                        "如果你需要更深入的了解某个方面，请告诉我。"
                    )
                )
            }

            userText.contains("错误", ignoreCase = true) || userText.contains("error", ignoreCase = true) ||
            userText.contains("bug", ignoreCase = true) -> {
                replyParts.add(
                    MessagePart.Text("让我帮你分析一下这个错误。根据你的描述，可能的原因有：\n\n" +
                        "- 空指针异常：检查变量是否在使用前已正确初始化\n" +
                        "- 类型不匹配：确认泛型类型是否一致\n" +
                        "- 生命周期问题：确保 ViewModel 中的操作不会在 Activity 销毁后继续执行\n\n" +
                        "建议添加以下调试代码：")
                )
                replyParts.add(
                    MessagePart.Code(
                        code = """try {
    // 你的代码
} catch (e: Exception) {
    Log.e("TAG", "Error: ${'$'}{e.message}", e)
    // 处理错误
}""",
                        language = "kotlin"
                    )
                )
            }

            else -> {
                val replies = listOf(
                    "我理解你的需求。这是一个很好的方向，让我来帮你实现。",
                    "收到！让我分析一下你的问题并提供解决方案。",
                    "好的，我来帮你处理这个任务。首先让我了解一下当前的项目状态。"
                )
                replyParts.add(MessagePart.Text(replies.random()))
            }
        }

        return Message(
            id = "msg-${UUID.randomUUID()}",
            sessionId = sessionId,
            role = MessageRole.ASSISTANT,
            parts = replyParts,
            createdAt = Instant.now(),
            model = ModelInfo(
                providerId = "openai",
                modelId = "gpt-4",
                displayName = "GPT-4"
            )
        )
    }
}
